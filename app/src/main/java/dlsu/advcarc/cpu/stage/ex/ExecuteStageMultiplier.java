package dlsu.advcarc.cpu.stage.ex;

import dlsu.advcarc.cpu.ALU;
import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Darren on 11/9/2015.
 */
public class ExecuteStageMultiplier extends AbstractExecuteStage {

    public ExecuteStageMultiplier(ExecuteStageSwitch executeStageSwitch, CPU cpu) {
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
    public void execute() {
        didRun = false;
        try {

            if (instruction == null)
                throw new Exception("Cannot run exMultiplier because there are no instructions yet.");

            if (currentNumberOfExecutionCycles >= numberOfExecutionCycles())
                throw new Exception("Finished running " + instruction);

            String inst = instruction.getInstructionOnly();

            // depending on instruction, perform operation on a, b, or imm
            EXMEM_ALUOutput = ALU.executeALU(inst, ir, a, b, imm, npc);

            // save values to cpu.hi cpu.lo
            StringBinary reg_hi = cpu.getREG_HI();
            StringBinary reg_lo = cpu.getREG_LO();
            cpu.setREG_HI(StringBinary.valueOf(0));
            cpu.setREG_LO(StringBinary.valueOf(0));

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

    public Memory getEXMEM_IR() {
        return ir;
    }

    public String getEXMEM_Cond() {
        return EXMEM_Cond;
    }

    public Parameter getEXMEM_B() {
        return EXMEM_B;
    }

    public StringBinary getEXMEM_ALUOutput() {
        return EXMEM_ALUOutput;
    }

    @Override
    public int numberOfExecutionCycles() {
        return 8;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public JsonArray toJsonArray() {
        return new JsonArray()
                .add(new JsonObject().put("register", "EX/MEM.MULT.B").put("value", getEXMEM_B() == null ? "null" : getEXMEM_B().getValue()))
                .add(new JsonObject().put("register", "EX/MEM.MULT.ALUOutput").put("value", getEXMEM_ALUOutput() == null ? "null" : getEXMEM_ALUOutput().toHexString()))
                .add(new JsonObject().put("register", "EX/MEM.MULT.Cond").put("value", getEXMEM_Cond() == null ? "null" : getEXMEM_Cond()))
                .add(new JsonObject().put("register", "EX/MEM.MULT.IR").put("value", getEXMEM_IR() == null ? "null" : getEXMEM_IR().getAsHex()));

    }


}
