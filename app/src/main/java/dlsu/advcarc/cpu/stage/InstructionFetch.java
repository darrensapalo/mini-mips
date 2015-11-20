package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.memory.*;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;

/**
 * Created by Darren on 11/9/2015.
 */
public class InstructionFetch extends Stage {

    private CPU cpu;
    private ProgramCode code;

    private String fetchedLine;

    private dlsu.advcarc.memory.Memory IFID_IR;
    private StringBinary IFID_NPC;
    private Execute executeStage;

    public InstructionFetch(CPU cpu, ProgramCode code) {
        this.cpu = cpu;
        this.code = code;
    }

    @Override
    protected void housekeeping() {

    }

    @Override
    public void execute() {
        int programCounter = cpu.getProgramCounter();
        fetchedLine = code.getCode(programCounter);

        // IF/ID.IR = Mem[PC]
        String memoryLocation = RadixHelper.convertLongToHexString(programCounter);
        IFID_IR = MemoryManager.instance().getInstance(memoryLocation);

        // IF/ID.NPC, PC = (EX/MEM.Cond) ? EX/MEM.ALUOutput : PC + 4;
        IFID_NPC = executeStage.getEXMEM_Cond().equals("1") ? executeStage.getEXMEM_ALUOutput() : new StringBinary(Integer.toBinaryString(programCounter + 4));
    }

    public String getFetchedLine() {
        return fetchedLine;
    }

    public void setExecuteStage(Execute executeStage) {
        this.executeStage = executeStage;
    }

    public Memory getIFID_IR() {
        return IFID_IR;
    }

    public StringBinary getIFID_NPC() {
        return IFID_NPC;
    }

}
