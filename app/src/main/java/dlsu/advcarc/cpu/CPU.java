package dlsu.advcarc.cpu;

import dlsu.advcarc.cpu.block.Block;
import dlsu.advcarc.cpu.stage.*;
import dlsu.advcarc.cpu.tracker.CPUCycleTracker;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;
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

    private Block dataDependencyBlock = Block.none();

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
            if (dataDependencyBlock.getBlockStage() <= Instruction.Stage.WB.ordinal()) {
                instructionFetchStage.execute();
                cycleTracker.setIfInstruction(instructionFetchStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {
            instructionFetchStage.reset();
            if (e.getMessage() != null)
                System.out.println("IF Stage: " + e.getMessage());
        }

        try {
            if (dataDependencyBlock.getBlockStage() <= Instruction.Stage.WB.ordinal()) {
                instructionDecodeStage.execute();
                cycleTracker.setIdInstruction(instructionDecodeStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println("ID Stage: " + e.getMessage());
        }

        try {
            if (dataDependencyBlock.getBlockStage() <= Instruction.Stage.WB.ordinal()) {
                executeStage.execute();
                cycleTracker.setExInstruction(executeStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println("EX Stage: " + e.getMessage());
        }

        try {
            if (dataDependencyBlock.getBlockStage() <= Instruction.Stage.WB.ordinal()) {
                memoryStage.execute();
                cycleTracker.setMemInstruction(memoryStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println("MEM Stage: " + e.getMessage());
        }
        try {
            if (dataDependencyBlock.getBlockStage() <= Instruction.Stage.WB.ordinal()) {
                writeBackStage.execute();
                cycleTracker.setWbInstruction(writeBackStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println("WB Stage: " + e.getMessage());
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

    public JsonObject toJsonObject() {
        return new JsonObject()
                .put("registers", getRegistersJsonArray())
                .put("pipeline", cycleTracker == null ? new JsonArray() : cycleTracker.toJsonArray());
    }


    public JsonArray getRegistersJsonArray() {

        if (instructionFetchStage == null)
            return new JsonArray();

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

    public Block getDataDependencyBlock() {
        return dataDependencyBlock;
    }

    public void setDataDependencyBlock(Instruction instruction, Instruction.Stage releaseStage, Instruction.Stage dataDependencyBlock) {
        this.dataDependencyBlock = new Block(instruction, releaseStage, dataDependencyBlock);
    }

    public void reviewBlock(Instruction instruction) {
        if (instruction.equals(dataDependencyBlock.getOwnedBy())){
            dataDependencyBlock = Block.none();
        }
    }
}
