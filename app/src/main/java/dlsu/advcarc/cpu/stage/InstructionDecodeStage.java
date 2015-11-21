package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.memory.*;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;

import java.util.ArrayList;

/**
 * Created by Darren on 11/9/2015.
 */
public class InstructionDecodeStage extends Stage {
    private InstructionFetchStage instructionFetchStage;

    private Memory IDEX_A;
    private Memory IDEX_B;
    private Memory IDEX_IMM;
    private Memory IDEX_IR;
    private StringBinary IDEX_NPC;

    public InstructionDecodeStage(InstructionFetchStage instructionFetchStage) {
        this.instructionFetchStage = instructionFetchStage;
    }

    @Override
    protected void housekeeping() {
        IDEX_IR = instructionFetchStage.getIFID_IR();
        IDEX_NPC = instructionFetchStage.getIFID_NPC();
    }

    @Override
    public void execute() {
        // instructionFetchStage.get code
        Memory ifid_ir = instructionFetchStage.getIFID_IR();

        String ifid_irAsBinary = ifid_ir.getAsBinary();

        String raw_A = ifid_irAsBinary.substring(6, 10);
        StringBinary binary_A = new StringBinary(raw_A);
        IDEX_A = MemoryManager.instance().getInstance(binary_A.toHexString());

        String raw_B = ifid_irAsBinary.substring(11, 15);
        StringBinary binary_B = new StringBinary(raw_B);
        IDEX_B = MemoryManager.instance().getInstance(binary_B.toHexString());

        String raw_IMM = ifid_irAsBinary.substring(16, 31);
        StringBinary binary_IMM = new StringBinary(raw_IMM);
        IDEX_IMM = MemoryManager.instance().getInstance(binary_IMM.toHexString());

        // Get references to registers
        Instruction instruction = new Instruction(new StringBinary(ifid_ir.getAsBinary()));

        ArrayList<Parameter> parameters = instruction.getParameters();

        for (Parameter p : parameters) {
            p.analyzeDependency();
        }

        housekeeping();
    }

    public Memory getIDEX_A() {
        return IDEX_A;
    }

    public Memory getIDEX_B() {
        return IDEX_B;
    }

    public Memory getIDEX_IMM() {
        return IDEX_IMM;
    }

    public Memory getIDEX_IR() {
        return IDEX_IR;
    }

    public StringBinary getIDEX_NPC() {
        return IDEX_NPC;
    }
}
