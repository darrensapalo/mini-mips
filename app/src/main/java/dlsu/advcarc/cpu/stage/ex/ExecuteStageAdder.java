package dlsu.advcarc.cpu.stage.ex;

import dlsu.advcarc.cpu.ALU;
import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.cpu.block.DataDependencyManager;
import dlsu.advcarc.dependency.DataDependencyException;
import dlsu.advcarc.parser.Instruction;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Darren on 11/9/2015.
 */
public class ExecuteStageAdder extends AbstractExecuteStage {

    public ExecuteStageAdder(ExecuteStageSwitch executeStageSwitch, CPU cpu) {
        super(cpu, executeStageSwitch);
    }

    @Override
    public void housekeeping() {
        super.housekeeping();

        if (instruction != null)
            System.out.println("EX Stage: Received a new instruction from ID stage - " + instruction);

        a = executeStageSwitch.getIDEX_A();
        b = executeStageSwitch.getIDEX_B();
        imm = executeStageSwitch.getIDEX_IMM();
        npc = executeStageSwitch.getIDEX_NPC();
    }

    @Override
    public int numberOfExecutionCycles() {
        return 4;
    }

    @Override
    public void execute() {
        didRun = false;
        try {
            if (instruction == null)
                throw new Exception();

            String inst = instruction.getInstructionOnly();

            // depending on instruction, perform operation on a, b, or imm
            EXMEM_ALUOutput = ALU.executeALU(inst, ir, a, b, imm, npc);

            // depending on instruction, compute for cond
            EXMEM_Cond = ALU.executeCond(inst, ir, a, b);

            didRun = true;

            if ("1".equals(EXMEM_Cond))
                System.out.println("The program will now branch because Cond evaluated to 1");

            super.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public JsonArray toJsonArray() {
        return new JsonArray()
                .add(new JsonObject().put("register", "EX/MEM.B").put("value", getEXMEM_B() == null ? "null" : getEXMEM_B().getValue()))
                .add(new JsonObject().put("register", "EX/MEM.ALUOutput").put("value", getEXMEM_ALUOutput() == null ? "null" : getEXMEM_ALUOutput().toHexString()))
                .add(new JsonObject().put("register", "EX/MEM.Cond").put("value", getEXMEM_Cond() == null ? "null" : getEXMEM_Cond()))
                .add(new JsonObject().put("register", "EX/MEM.IR").put("value", getEXMEM_IR() == null ? "null" : getEXMEM_IR().getAsHex()));

    }


}
