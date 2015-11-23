package dlsu.advcarc.cpu;

import dlsu.advcarc.cpu.block.Block;
import dlsu.advcarc.cpu.stage.*;
import dlsu.advcarc.cpu.tracker.CPUCycleTracker;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.server.Addresses;
import dlsu.advcarc.server.EventBusHolder;
import dlsu.advcarc.utils.RadixHelper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;

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
    private boolean fetchFailed;

    private ArrayList<Instruction> forDequeuing = new ArrayList<>();
    private Block removeBlock;

    public void input(ProgramCode code) {
        this.code = code;
        programCounter = RadixHelper.convertLongToStringBinary(code.InitialProgramCounter());
        code.writeToMemory();

        instructionFetchStage = new InstructionFetchStage(this, code);
        instructionDecodeStage = new InstructionDecodeStage(this, instructionFetchStage);
        executeStage = new ExecuteStage(this, instructionDecodeStage, instructionFetchStage);
        memoryStage = new MemoryStage(this, executeStage);
        writeBackStage = new WriteBackStage(this, memoryStage);

        cycleTracker = new CPUCycleTracker();
    }

    public void clock() {
        System.out.println("Cycle " + cycleTracker.getCycleNumber());

        writeBackStage.incrementInstruction();
        memoryStage.incrementInstruction();
        executeStage.incrementInstruction();
        instructionDecodeStage.incrementInstruction();
        instructionFetchStage.incrementInstruction();

        housekeeping();

        stages();


        removeDependencesIfAny();

        // Remove data block if any
        if (removeBlock != null)
            dataDependencyBlock = removeBlock;
        removeBlock = null;


        // Broadcast
        cycleTracker.nextCycle();

        JsonObject o = this.toJsonObject();
        EventBusHolder.instance().getEventBus()
                .publish(Addresses.CPU_BROADCAST, o);

        System.out.println();
    }

    private void removeDependencesIfAny() {
        Iterator<Instruction> iterator = forDequeuing.iterator();
        while (iterator.hasNext()) {
            Instruction instruction = iterator.next();
            for (Parameter p : instruction.getParameters()) {
                p.dequeueDependency();
            }
        }
        forDequeuing.clear();
    }

    private void housekeeping() {
        if (dataDependencyBlock.canHousekeepingRun(writeBackStage, memoryStage))
            writeBackStage.housekeeping();

        if (dataDependencyBlock.canHousekeepingRun(memoryStage, executeStage))
            memoryStage.housekeeping();

        if (dataDependencyBlock.canHousekeepingRun(executeStage, instructionDecodeStage))
            executeStage.housekeeping();

        if (dataDependencyBlock.canHousekeepingRun(instructionDecodeStage, instructionFetchStage))
            instructionDecodeStage.housekeeping();

        if (fetchFailed)
            instructionFetchStage.reset();
    }

    private void stages() {

        System.out.println();
        System.out.println("-- WB stage");
        try {
            if (dataDependencyBlock.canStageRun(writeBackStage, instructionFetchStage)) {
                writeBackStage.execute();
                cycleTracker.setWbInstruction(writeBackStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println("WB Stage: " + e.getMessage());
        }


        System.out.println();
        System.out.println("-- MEM stage");
        try {
            if (dataDependencyBlock.canStageRun(memoryStage, instructionFetchStage)) {
                memoryStage.execute();
                cycleTracker.setMemInstruction(memoryStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println("MEM Stage: " + e.getMessage());
        }


        System.out.println();
        System.out.println("-- EX stage");
        try {
            if (dataDependencyBlock.canStageRun(executeStage, instructionFetchStage)) {
                executeStage.execute();
                cycleTracker.setExInstruction(executeStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println("EX Stage: " + e.getMessage());
        }


        System.out.println();
        System.out.println("-- ID stage");
        try {
            if (dataDependencyBlock.canStageRun(instructionDecodeStage, instructionFetchStage)) {
                instructionDecodeStage.execute();
                cycleTracker.setIdInstruction(instructionDecodeStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println("ID Stage: " + e.getMessage());
        }

        System.out.println("-- IF stage");
        try {
            if (dataDependencyBlock.canStageRun(instructionFetchStage, null)) {
                instructionFetchStage.execute();
                cycleTracker.setIfInstruction(instructionFetchStage.getInstruction().getInstruction());
            }
        } catch (Exception e) {
            fetchFailed = true;

            if (e.getMessage() != null)
                System.out.println("IF Stage: " + e.getMessage());
        }

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
        forDequeuing.add(instruction);

        if (instruction.equals(dataDependencyBlock.getOwnedBy())) {
            removeBlock = Block.none();
        }
    }
}
