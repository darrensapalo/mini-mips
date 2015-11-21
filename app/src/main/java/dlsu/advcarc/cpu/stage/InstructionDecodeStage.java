package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.immediate.register.Immediate;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.Register;
import dlsu.advcarc.register.RegisterManager;

import java.util.ArrayList;

/**
 * Created by Darren on 11/9/2015.
 */
public class InstructionDecodeStage extends Stage {
    private InstructionFetchStage instructionFetchStage;

    private Register IDEX_A;
    private Register IDEX_B;
    private Immediate IDEX_IMM;
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
        IDEX_A = RegisterManager.instance().getInstance(binary_A.toHexString());

        String raw_B = ifid_irAsBinary.substring(11, 15);
        StringBinary binary_B = new StringBinary(raw_B);
        IDEX_B = RegisterManager.instance().getInstance(binary_B.toHexString());

        String raw_IMM = ifid_irAsBinary.substring(16, 31);

        // sign extend
        String sign = raw_IMM.substring(0, 1);
        for (int i = 0; i < 48; i++)
            raw_IMM = sign + raw_IMM;
        IDEX_IMM = new Immediate(new StringBinary(raw_IMM));


        // Get references to registers
        Instruction instruction = new Instruction(new StringBinary(ifid_ir.getAsBinary()));

        ArrayList<Parameter> parameters = instruction.getParameters();

        for (Parameter p : parameters) {
            p.analyzeDependency();
        }

        housekeeping();
    }

    public Register getIDEX_A() {
        return IDEX_A;
    }

    public Register getIDEX_B() {
        return IDEX_B;
    }

    public Immediate getIDEX_IMM() {
        return IDEX_IMM;
    }

    public Memory getIDEX_IR() {
        return IDEX_IR;
    }

    public StringBinary getIDEX_NPC() {
        return IDEX_NPC;
    }
}
