package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.ALU;
import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.opcode.OpcodeHelper;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.StringBinary;

/**
 * Created by Darren on 11/9/2015.
 */
public class ExecuteStage extends Stage {
    private InstructionDecodeStage instructionDecodeStage;
    private Memory EXMEM_IR;
    private String EXMEM_Cond;
    private Memory EXMEM_B;
    private StringBinary EXMEM_ALUOutput;

    public ExecuteStage(InstructionDecodeStage instructionDecodeStage, InstructionFetchStage instructionFetchStage) {
        this.instructionDecodeStage = instructionDecodeStage;
        instructionFetchStage.setExecuteStage(this);
    }

    @Override
    protected void housekeeping() {
        EXMEM_IR = instructionDecodeStage.getIDEX_IR();
        EXMEM_B = instructionDecodeStage.getIDEX_B();
    }

    @Override
    public void execute() {
        housekeeping();

        Memory a = instructionDecodeStage.getIDEX_A();
        Memory b = instructionDecodeStage.getIDEX_B();
        Memory imm = instructionDecodeStage.getIDEX_IMM();
        StringBinary npc = instructionDecodeStage.getIDEX_NPC();

        String instruction = OpcodeHelper.getInstruction(new StringBinary(EXMEM_IR.getAsBinary()));

        // depending on instruction, perform operation on a, b, or imm
        // EXMEM_ALUOutput =
        EXMEM_ALUOutput = ALU.executeALU(instruction, EXMEM_IR, a, b, imm, npc);

        // depending on instruction, compute for cond
        EXMEM_Cond = ALU.executeCond(instruction, EXMEM_IR, a, b);
    }

    public Memory getEXMEM_IR() {
        return EXMEM_IR;
    }

    public String getEXMEM_Cond() {
        return EXMEM_Cond;
    }

    public Memory getEXMEM_B() {
        return EXMEM_B;
    }

    public StringBinary getEXMEM_ALUOutput() {
        return EXMEM_ALUOutput;
    }
}
