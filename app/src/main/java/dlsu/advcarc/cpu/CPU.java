package dlsu.advcarc.cpu;

import dlsu.advcarc.cpu.stage.*;
import dlsu.advcarc.parser.ProgramCode;

/**
 * Created by Darren on 11/9/2015.
 */
public class CPU {
    InstructionFetch instructionFetchStage = null;
    InstructionDecode instructionDecodeStage = null;
    Execute executeStage = null;
    Memory memoryStage = null;
    WriteBack writeBackStage = null;

    private ProgramCode code;

    private int programCounter;

    public void input(ProgramCode code){
        this.code = code;
        programCounter = code.InitialProgramCounter();

        instructionFetchStage = new InstructionFetch(this, code);
        instructionDecodeStage = new InstructionDecode(this, instructionFetchStage);
        executeStage = new Execute(this, instructionDecodeStage);
        memoryStage = new Memory(this, executeStage);
        writeBackStage = new WriteBack(this);
    }

    public void clock(){
        try {
            instructionFetchStage.execute();
            instructionDecodeStage.execute();
            executeStage.execute();
            memoryStage.execute();
            writeBackStage.execute();
        }catch (NullPointerException e){
            throw new IllegalStateException("Cannot run the CPU when the resources are not yet initialized. Please input a ProgramCode.");
        }
    }

    public int getProgramCounter() {
        return programCounter;
    }
}
