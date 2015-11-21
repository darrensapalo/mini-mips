package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.immediate.register.Immediate;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.Register;

import java.util.ArrayList;

/**
 * Created by Darren on 11/9/2015.
 */
public class InstructionDecodeStage extends Stage {
    private CPU cpu;
    private InstructionFetchStage instructionFetchStage;

    private Parameter IDEX_A;
    private Parameter IDEX_B;
    private Parameter IDEX_IMM;
    private Memory IDEX_IR;
    private StringBinary IDEX_NPC;
    private Immediate newImm;
    private Instruction instruction;

    public InstructionDecodeStage(CPU cpu, InstructionFetchStage instructionFetchStage) {
        this.cpu = cpu;
        this.instructionFetchStage = instructionFetchStage;
    }

    @Override
    public void housekeeping() {
        IDEX_IR = instructionFetchStage.getIFID_IR();
        IDEX_NPC = instructionFetchStage.getIFID_NPC();
        instruction = instructionFetchStage.getInstruction();
    }

    @Override
    public void execute() {
        // instructionFetchStage.get code

        ArrayList<Parameter> parameters = instruction.getParameters();

        for (Parameter p : parameters) {
            if (p.getParameter() instanceof Register)
                p.analyzeDependency();
        }

        for (Parameter p : parameters) {
            if (p.getParameter() instanceof Register)
                if (instruction.equals(p.peekDependency()) == false) {
                    cpu.setDataDependencyBlock(1);
                    return;
                }
        }

        try {
            IDEX_A = parameters.get(2);
        } catch (Exception e) {

        }

        try {
            IDEX_B = parameters.get(0);
        } catch (Exception e) {

        }

        try {
            IDEX_IMM = parameters.get(1);
        } catch (Exception e) {

        }

    }

    public Parameter getIDEX_A() {
        return IDEX_A;
    }

    public Parameter getIDEX_B() {
        return IDEX_B;
    }

    public Parameter getIDEX_IMM() {
        return IDEX_IMM;
    }

    public Memory getIDEX_IR() {
        return IDEX_IR;
    }

    public StringBinary getIDEX_NPC() {
        return IDEX_NPC;
    }

    public Instruction getInstruction() {
        return instruction;
    }
}
