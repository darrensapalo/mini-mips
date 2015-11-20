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
public class InstructionDecode extends Stage {
    private CPU cpu;
    private InstructionFetch instructionFetchStage;
    private Instruction instruction;
    private String IDEX_A;
    private String IDEX_B;
    private String IDEX_IMM;
    private Memory IDEX_IR;
    private StringBinary IDEX_NPC;

    public InstructionDecode(CPU cpu, InstructionFetch instructionFetchStage) {
        this.cpu = cpu;
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
        dlsu.advcarc.memory.Memory ifid_ir = instructionFetchStage.getIFID_IR();

        String ifid_irAsBinary = ifid_ir.getAsBinary();

        IDEX_A = ifid_irAsBinary.substring(6, 10);
        IDEX_B = ifid_irAsBinary.substring(11, 15);
        IDEX_IMM = ifid_irAsBinary.substring(16, 31);

        String fetchedLine = instructionFetchStage.getFetchedLine();

        // Get references to registers
        instruction = new Instruction(fetchedLine);

        ArrayList<Parameter> parameters = instruction.getParameters();

        for (Parameter p : parameters) {
            p.analyzeDependency();
        }

        housekeeping();
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public String getIDEX_A() {
        return IDEX_A;
    }

    public String getIDEX_B() {
        return IDEX_B;
    }

    public String getIDEX_IMM() {
        return IDEX_IMM;
    }

    public Memory getIDEX_IR() {
        return IDEX_IR;
    }

    public StringBinary getIDEX_NPC() {
        return IDEX_NPC;
    }
}
