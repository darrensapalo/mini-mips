package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.StringBinary;

/**
 * Created by Darren on 11/9/2015.
 */
public class Execute extends Stage {
    private final CPU cpu;
    private InstructionDecode instructionDecodeStage;
    private Memory EXMEM_IR;
    private String EXMEM_Cond;
    private String EXMEM_B;
    private StringBinary EXMEM_ALUOutput;
    private Instruction instruction;

    public Execute(CPU cpu, InstructionDecode instructionDecodeStage, InstructionFetch instructionFetchStage) {
        this.cpu = cpu;
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

        String a = instructionDecodeStage.getIDEX_A();
        String b = instructionDecodeStage.getIDEX_B();
        String imm = instructionDecodeStage.getIDEX_IMM();
        StringBinary npc = instructionDecodeStage.getIDEX_NPC();

        instruction = instructionDecodeStage.getInstruction();



        // depending on instruction, perform operation on a, b, or imm
        // EXMEM_ALUOutput =
        EXMEM_ALUOutput = cpu.executeALU(instruction, EXMEM_IR, a, b, imm, npc);

        // depending on instruction, compute for cond
        EXMEM_Cond = cpu.executeCond(instruction, EXMEM_IR, a, b);
    }

    public Memory getEXMEM_IR() {
        return EXMEM_IR;
    }

    public String getEXMEM_Cond() {
        return EXMEM_Cond;
    }

    public String getEXMEM_B() {
        return EXMEM_B;
    }

    public StringBinary getEXMEM_ALUOutput() {
        return EXMEM_ALUOutput;
    }

    public Instruction getInstruction() {
        return instruction;
    }
}
