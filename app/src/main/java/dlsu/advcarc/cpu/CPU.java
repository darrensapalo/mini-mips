package dlsu.advcarc.cpu;

import dlsu.advcarc.cpu.stage.*;
import dlsu.advcarc.parser.ProgramCode;

/**
 * Created by Darren on 11/9/2015.
 */
public class CPU {
    Stage instructionFetchStage = null;
    Stage instructionDecodeStage = null;
    Stage executeStage = null;
    Stage memoryStage = null;
    Stage writeBackStage = null;

    private ProgramCode code;

    private int programCounter;

    public void input(ProgramCode code){
        this.code = code;
        programCounter = code.InitialProgramCounter();

        instructionFetchStage = new InstructionFetch(this, code);
        instructionDecodeStage = new InstructionDecode(this);
        executeStage = new Execute(this);
        memoryStage = new Memory(this);
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
