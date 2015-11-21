package dlsu.advcarc.cpu;

import dlsu.advcarc.cpu.stage.*;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;

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

    private int dataDependencyBlock = -1;

    private StringBinary programCounter;

    public void input(ProgramCode code) {
        this.code = code;
        programCounter = RadixHelper.convertLongToStringBinary(code.InitialProgramCounter());
        code.writeToMemory();

        instructionFetchStage = new InstructionFetchStage(this, code);
        instructionDecodeStage = new InstructionDecodeStage(this, instructionFetchStage);
        executeStage = new ExecuteStage(instructionDecodeStage, instructionFetchStage);
        memoryStage = new MemoryStage(this, executeStage);
        writeBackStage = new WriteBackStage(this, memoryStage);
    }

    public void clock() {

        try {
            if (dataDependencyBlock <= 0)
                instructionFetchStage.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (dataDependencyBlock <= 1)
                instructionDecodeStage.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (dataDependencyBlock <= 2)
                executeStage.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (dataDependencyBlock <= 3)
                memoryStage.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (dataDependencyBlock <= 4)
                writeBackStage.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


        writeBackStage.housekeeping();

        memoryStage.housekeeping();
        executeStage.housekeeping();

        instructionDecodeStage.housekeeping();
        instructionFetchStage.housekeeping();
    }

    public StringBinary getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(StringBinary programCounter) {
        this.programCounter = programCounter;
    }

    public int getDataDependencyBlock() {
        return dataDependencyBlock;
    }

    public void setDataDependencyBlock(int dataDependencyBlock) {
        this.dataDependencyBlock = dataDependencyBlock;
    }
}
