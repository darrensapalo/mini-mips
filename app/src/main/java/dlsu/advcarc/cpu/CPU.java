package dlsu.advcarc.cpu;

import dlsu.advcarc.cpu.stage.*;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;

/**
 * Created by Darren on 11/9/2015.
 */
public class CPU {
    InstructionFetchStage instructionFetchStage = null;
    InstructionDecodeStage instructionDecodeStage = null;
    ExecuteStage executeStage = null;
    MemoryStage memoryStage = null;
    WriteBackStage writeBackStage = null;

    private ProgramCode code;

    private int programCounter;

    public void input(ProgramCode code){
        this.code = code;
        programCounter = code.InitialProgramCounter();

        instructionFetchStage = new InstructionFetchStage(this, code);
        instructionDecodeStage = new InstructionDecodeStage(instructionFetchStage);
        executeStage = new ExecuteStage(instructionDecodeStage, instructionFetchStage);
        memoryStage = new MemoryStage(executeStage);
        writeBackStage = new WriteBackStage(memoryStage);
    }

    public void clock(){
        try {
            instructionFetchStage.execute();
            instructionDecodeStage.execute();
            executeStage.execute();
            memoryStage.execute();
            writeBackStage.execute();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            throw new IllegalStateException("Cannot run the CPU when the resources are not yet initialized. Please input a ProgramCode.");
        }
    }

    public int getProgramCounter() {
        return programCounter;
    }
}
