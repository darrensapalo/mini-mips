package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.opcode.OpcodeHelper;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.Register;

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
    private Instruction instruction;
    private Parameter MEMWB_B;

    public MemoryStage(CPU cpu, ExecuteStage executeStage) {
        this.cpu = cpu;
        this.executeStage = executeStage;
    }

    @Override
    public void housekeeping() {

        // ALU instructions
        MEMWB_IR = executeStage.getEXMEM_IR();
        MEMWB_ALUOutput = executeStage.getEXMEM_ALUOutput();
        instruction = executeStage.getInstruction();
        MEMWB_B = executeStage.getEXMEM_B();
    }

    @Override
    public void execute() {

        ArrayList<Parameter> parameters = this.instruction.getParameters();
        // Analyze the dependencies of memory references
        for (Parameter p : parameters) {
            if (p.getParameter() instanceof Memory)
                p.analyzeDependency();
        }

        // If I have dependencies, block
        for (Parameter p : parameters) {
            if (p.getParameter() instanceof Memory) {
                Instruction dependentOnThis = p.peekDependency();
                if (this.instruction.equals(dependentOnThis) == false) {
                    cpu.setDataDependencyBlock(3);
                    return;
                }
            }
        }

        switch (instruction.getInstruction()) {
            // read
            case "LW":
            case "LWU":
                MEMWB_LMD = MemoryManager.instance().getInstance(MEMWB_ALUOutput.toHexString());
                break;

            // write
            case "SW":
            case "L.S":
            case "S.S":
                MemoryManager.instance().updateMemory(MEMWB_ALUOutput.toHexString(), MEMWB_B.getParameter().read());
                break;
        }

        // If I have dependencies, block
        for (Parameter p : parameters) {
            if (p.getParameter() instanceof Memory) {
                p.dequeueDependency();
            }
        }

        // todo: perform branch completion in IF stage
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

    public Instruction getInstruction() {
        return instruction;
    }
}
