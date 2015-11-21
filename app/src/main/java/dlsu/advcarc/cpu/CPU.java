package dlsu.advcarc.cpu;

import dlsu.advcarc.cpu.stage.*;
import dlsu.advcarc.cpu.tracker.CPUCycleTracker;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.server.handlers.CPUClockHandler;
import dlsu.advcarc.utils.RadixHelper;
import dlsu.advcarc.server.Addresses;
import dlsu.advcarc.server.EventBusHolder;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

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

    private CPUCycleTracker cycleTracker;

    public void input(ProgramCode code) {
        this.code = code;
        programCounter = RadixHelper.convertLongToStringBinary(code.InitialProgramCounter());
        code.writeToMemory();

        instructionFetchStage = new InstructionFetchStage(this, code);
        instructionDecodeStage = new InstructionDecodeStage(this, instructionFetchStage);
        executeStage = new ExecuteStage(instructionDecodeStage, instructionFetchStage);
        memoryStage = new MemoryStage(this, executeStage);
        writeBackStage = new WriteBackStage(this, memoryStage);

        cycleTracker = new CPUCycleTracker();
    }

    public void clock() {

        try {
            if (dataDependencyBlock <= 0) {
                instructionFetchStage.execute();
                cycleTracker.setIfInstruction(instructionFetchStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {

        }

        try {
            if (dataDependencyBlock <= 1) {
                instructionDecodeStage.execute();
                cycleTracker.setIdInstruction(instructionDecodeStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {

        }

        try {
            if (dataDependencyBlock <= 2){
                executeStage.execute();
                cycleTracker.setExInstruction(executeStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {

        }

        try {
            if (dataDependencyBlock <= 3) {
                memoryStage.execute();
                cycleTracker.setMemInstruction(memoryStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {

        }
        try {
            if (dataDependencyBlock <= 4) {
                writeBackStage.execute();
                cycleTracker.setWbInstruction(writeBackStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {

        }


        writeBackStage.housekeeping();

        memoryStage.housekeeping();
        executeStage.housekeeping();

        instructionDecodeStage.housekeeping();
        instructionFetchStage.housekeeping();

        cycleTracker.nextCycle();

        EventBusHolder.instance().getEventBus()
                .publish(Addresses.CPU_BROADCAST, this.toJsonObject());

    }

    public StringBinary getProgramCounter() {
        return programCounter;
    }

    public JsonObject toJsonObject(){
        return new JsonObject()
                .put("registers", getRegistersJsonArray())
                .put("pipeline", cycleTracker.toJsonArray());
    }


    public JsonArray getRegistersJsonArray(){
        return new JsonArray()
                .addAll(instructionFetchStage.toJsonArray())
                .addAll(instructionDecodeStage.toJsonArray())
                .addAll(executeStage.toJsonArray())
                .addAll(memoryStage.toJsonArray())
                ;
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
