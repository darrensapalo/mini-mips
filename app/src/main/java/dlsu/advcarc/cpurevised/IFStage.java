package dlsu.advcarc.cpurevised;

import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.Code;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;

/**
 * Created by user on 11/29/2015.
 */
public class IFStage implements CPUStage{


    private CPU cpu;
    private Opcode IR;

    private StringBinary NPC;
    private StringBinary PC;




    public IFStage(CPU cpu){
        this.cpu = cpu;
        NPC = new StringBinary("0");
        PC = new StringBinary("0");
    }

    @Override
    public boolean hasInstructionToForward() {
        return false;
    }

    @Override
    public void execute() {
        IR = new Opcode(new StringBinary(MemoryManager.instance().getInstance(PC.toHexString(4)).getAsBinary()));
    }
}
