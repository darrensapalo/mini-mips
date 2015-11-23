package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;

/**
 * Created by Darren on 11/9/2015.
 */
public class MemoryStage extends Stage {
    private CPU cpu;
    private ExecuteStage executeStage;

    private StringBinary MEMWB_ALUOutput;
    private Memory MEMWB_LMD;
    private dlsu.advcarc.memory.Memory MEMWB_IR;
    private Parameter MEMWB_B;

    public MemoryStage(CPU cpu, ExecuteStage executeStage) {
        this.cpu = cpu;
        this.executeStage = executeStage;
        stageId = 3;
    }

    @Override
    public void housekeeping() {
        try {
            isBlocked = isBlocked || executeStage.isBlocked();
            if (isBlocked == false) {

                Instruction inst = executeStage.getInstruction();

                // ALU instructions
                MEMWB_IR = executeStage.getEXMEM_IR();
                MEMWB_ALUOutput = executeStage.getEXMEM_ALUOutput();
                instruction = inst;
                MEMWB_B = executeStage.getEXMEM_B();
            }
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage());
        }

    }

    @Override
    public void execute() {
        didRun = false;
        checkDependencies(instruction);


        // Analyze the dependencies of memory references
        ArrayList<Parameter> parameters = this.instruction.getParameters();

        for (Parameter p : parameters) {
            if (p.getParameter() instanceof Memory)
                p.analyzeDependency();
        }


        String instruction = this.instruction.getInstructionOnly();
        switch (instruction) {
            // read
            case "LW":
            case "LWU":
                System.out.println("MEM Stage: Reading from address " + MEMWB_ALUOutput.toHexString());
                MEMWB_LMD = MemoryManager.instance().getInstance(MEMWB_ALUOutput.toHexString());
                break;

            // write
            case "SW":
            case "L.S":
            case "S.S":
                System.out.println("MEM Stage: Writing to address " + MEMWB_ALUOutput.toHexString() + " with value " + MEMWB_B.getParameter().read().toHexString());
                MemoryManager.instance().updateMemory(MEMWB_ALUOutput.toHexString(), MEMWB_B.getParameter().read());
                break;
        }

        // If I have dependencies, block
        for (Parameter p : parameters) {
            if (p.getParameter() instanceof Memory) {
                p.dequeueDependency();
            }
        }

        if (cpu.getDataDependencyBlock().getReleaseStage() == Instruction.Stage.MEM)
            cpu.reviewBlock(this.instruction);

        // todo: perform branch completion in IF stage

        didRun = true;
    }


    private void checkDependencies(Instruction instruction) {
        // If I have dependencies, block
        for (Parameter p : instruction.getParameters()) {
            if (p.getParameter() instanceof Memory) {
                Instruction dependentOnThis = p.peekDependency();
                if (dependentOnThis != null && !instruction.equals(dependentOnThis)) {
                    cpu.setDataDependencyBlock(dependentOnThis, Instruction.Stage.MEM, Instruction.Stage.MEM);
                    throw new IllegalStateException("Cannot proceed because " + instruction.toString() + " has a write dependency on " + dependentOnThis.toString());
                }
            }
        }
    }

    public StringBinary getMEMWB_ALUOutput() {
        return MEMWB_ALUOutput;
    }

    public Memory getMEMWB_LMD() {
        return MEMWB_LMD;
    }

    public Memory getMEMWB_IR() {
        return MEMWB_IR;
    }


    public JsonArray toJsonArray() {
        return new JsonArray()
                .add(new JsonObject().put("register", "MEM/WB.LMD").put("value", getMEMWB_LMD() == null ? "null" : getMEMWB_LMD().getAsHex()))
                .add(new JsonObject().put("register", "MEM/WB.ALUOutput").put("value", getMEMWB_ALUOutput() == null ? "null" : getMEMWB_ALUOutput().toHexString()))
                .add(new JsonObject().put("register", "MEM/WB.IR").put("value", getMEMWB_IR() == null ? "null" : getMEMWB_IR().getAsHex()));
    }

}
