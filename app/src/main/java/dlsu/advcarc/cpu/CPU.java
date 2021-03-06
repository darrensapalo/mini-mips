package dlsu.advcarc.cpu;

import dlsu.advcarc.cpu.block.ControlHazardManager;
import dlsu.advcarc.cpu.block.DataDependencyManager;
import dlsu.advcarc.cpu.stage.*;
import dlsu.advcarc.cpu.stage.ex.ExecuteStageSwitch;
import dlsu.advcarc.cpu.tracker.CPUCycleTracker;
import dlsu.advcarc.dependency.DataDependencyException;
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
    ExecuteStageSwitch executeStage = null;
    MemoryStage memoryStage = null;
    WriteBackStage writeBackStage = null;

    private ProgramCode code;

    private DataDependencyManager dataDependencyManager = new DataDependencyManager();

    private StringBinary programCounter;

    private CPUCycleTracker cycleTracker;
    private boolean fetchFailed;

    private ArrayList<Instruction> forDequeuing = new ArrayList<>();
    private ControlHazardManager controlHazardManager = new ControlHazardManager();

    private StringBinary REG_HI, REG_LO;

    public void input(ProgramCode code) {
        this.code = code;
        programCounter = RadixHelper.convertLongToStringBinary(code.InitialProgramCounter());
        code.writeToMemory();

        instructionFetchStage = new InstructionFetchStage(this, code);
        instructionDecodeStage = new InstructionDecodeStage(this, instructionFetchStage);
        executeStage = new ExecuteStageSwitch(this, instructionDecodeStage, instructionFetchStage);
        memoryStage = new MemoryStage(this, executeStage);
        writeBackStage = new WriteBackStage(this, memoryStage);

        REG_HI = StringBinary.valueOf(0);
        REG_LO = StringBinary.valueOf(0);

        cycleTracker = new CPUCycleTracker(code);

        broadcastCPUState();
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

        // Broadcast
        cycleTracker.nextCycle();
        broadcastCPUState();

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
        if (writeBackStage.canHousekeepingRun(memoryStage, dataDependencyManager))
            writeBackStage.housekeeping();

        if (memoryStage.canHousekeepingRun(executeStage, dataDependencyManager))
            memoryStage.housekeeping();

        if (executeStage.canHousekeepingRun(instructionDecodeStage, dataDependencyManager))
            executeStage.housekeeping();

        if (instructionDecodeStage.canHousekeepingRun(instructionFetchStage, dataDependencyManager))
            instructionDecodeStage.housekeeping();


    }

    private void stages() {

        ControlHazardManager controlHazardManager = getControlHazardManager();
        Instruction branchInstruction = controlHazardManager.checkBranch();

        // When the branch instruction is finished, finish off the branching by executing a single IF cycle
        if (branchInstruction != null && branchInstruction.getStage() != Instruction.Stage.ID) {
            Instruction.Stage branchCurrentStage = branchInstruction.getStage();
            if (branchCurrentStage == Instruction.Stage.MEM) {
                System.out.println("-- IF stage");
                try {
                    if (instructionFetchStage.canStageRun(dataDependencyManager)) {
                        instructionFetchStage.execute();
                        cycleTracker.setIfInstruction(instructionFetchStage.getInstruction());
                    }
                } catch (Exception e) {

                    fetchFailed = true;

                    if (e.getMessage() != null)
                        System.out.println("IF Stage: " + e.getMessage());
                }

                System.out.println();
                System.out.println("-- MEM stage");
                try {
                    if (memoryStage.canStageRun(dataDependencyManager)) {
                        memoryStage.execute();
                        cycleTracker.setMemInstruction(memoryStage.getInstruction());
                    }
                } catch (DataDependencyException e) {
                    e.handle(this);
                } catch (Exception e) {
                    if (e.getMessage() != null)
                        System.out.println("MEM Stage: " + e.getMessage());
                }

                controlHazardManager.finish();

            } else if (branchCurrentStage == Instruction.Stage.EX) {
                System.out.println("-- IF stage");
                try {
                    if (instructionFetchStage.canStageRun(dataDependencyManager)) {
                        instructionFetchStage.execute();
                        cycleTracker.setIfInstruction(instructionFetchStage.getInstruction());
                    }
                } catch (Exception e) {
                    fetchFailed = true;

                    if (e.getMessage() != null)
                        System.out.println("IF Stage: " + e.getMessage());
                }

                try {
                    if (executeStage.canStageRun(dataDependencyManager)) {
                        executeStage.execute();

                        try {
                            if (executeStage.getExInteger().didRun())
                                cycleTracker.setExInstruction(executeStage.getExInteger().getInstruction());
                        } catch (Exception e) {

                        }

                        try {
                            if (executeStage.getExAdder().didRun())
                                cycleTracker.setExInstruction(executeStage.getExAdder().getInstruction());
                        } catch (Exception e) {

                        }

                        try {
                            if (executeStage.getExMultiplier().didRun())
                                cycleTracker.setExInstruction(executeStage.getExMultiplier().getInstruction());
                        } catch (Exception e) {

                        }
                    }
                } catch (Exception e) {
                    if (e.getMessage() != null)
                        System.out.println("EX Stage: " + e.getMessage());
                }

            }
        } else {

            System.out.println();
            System.out.println("-- WB stage");
            try {
                if (writeBackStage.canStageRun(dataDependencyManager)) {
                    writeBackStage.execute();
                    cycleTracker.setWbInstruction(writeBackStage.getInstruction());
                }
            } catch (DataDependencyException e) {
                e.handle(this);
            } catch (Exception e) {

                if (e.getMessage() != null)
                    System.out.println("WB Stage: " + e.getMessage());
            }


            System.out.println();
            System.out.println("-- MEM stage");
            try {
                if (memoryStage.canStageRun(dataDependencyManager)) {
                    memoryStage.execute();
                    cycleTracker.setMemInstruction(memoryStage.getInstruction());
                }
            } catch (DataDependencyException e) {
                e.handle(this);
            } catch (Exception e) {
                if (e.getMessage() != null)
                    System.out.println("MEM Stage: " + e.getMessage());
            }


            System.out.println();
            System.out.println("-- EX stage");
            try {
                if (executeStage.canStageRun(dataDependencyManager)) {
                    executeStage.execute();

                    try {
                        if (executeStage.getExInteger().didRun())
                            cycleTracker.setExInstruction(executeStage.getExInteger().getInstruction());
                    } catch (Exception e) {

                    }

                    try {
                        if (executeStage.getExAdder().didRun())
                            cycleTracker.setExInstruction(executeStage.getExAdder().getInstruction());
                    } catch (Exception e) {

                    }

                    try {
                        if (executeStage.getExMultiplier().didRun())
                            cycleTracker.setExInstruction(executeStage.getExMultiplier().getInstruction());
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e) {
                if (e.getMessage() != null)
                    System.out.println("EX Stage: " + e.getMessage());
            }


            System.out.println();
            System.out.println("-- ID stage");
            try {
                if (instructionDecodeStage.canStageRun(dataDependencyManager)) {
                    instructionDecodeStage.execute();
                    cycleTracker.setIdInstruction(instructionDecodeStage.getInstruction());
                }
            } catch (DataDependencyException e) {
                e.handle(this);

            } catch (Exception e) {
                if (e.getMessage() != null)
                    System.out.println("ID Stage: " + e.getMessage());
            }

            System.out.println("-- IF stage");
            try {
                if (instructionFetchStage.canStageRun(dataDependencyManager)) {
                    instructionFetchStage.execute();
                    cycleTracker.setIfInstruction(instructionFetchStage.getInstruction());
                }
            } catch (Exception e) {
                fetchFailed = true;
                e.printStackTrace();
                if (e.getMessage() != null)
                    System.out.println("IF Stage: " + e.getMessage());
            }
        }

    }

    public StringBinary getProgramCounter() {
        return programCounter;
    }

    public void broadcastCPUState() {
        EventBusHolder.instance().getEventBus()
                .publish(Addresses.CPU_BROADCAST, this.toJsonObject());
    }


    public JsonObject toJsonObject() {
        return new JsonObject()
                .put("registers", getRegistersJsonArray())
                .put("pipeline", cycleTracker == null ? new JsonArray() : cycleTracker.toJsonObject());
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

    public DataDependencyManager getDataDependencyManager() {
        return dataDependencyManager;
    }

    /**
     * This function is called when an instruction is finished with the lock
     * on a data dependency. If the instruction owns the data dependency, then the
     * data dependency is removed.
     *
     * @param instruction
     */
    public void reviewBlock(Instruction instruction) {
        forDequeuing.add(instruction);
        dataDependencyManager.removeBlockOwnedBy(instruction);
    }

    public void addDataDependencyBlock(DataDependencyManager.DataDependencyBlock block) {
        dataDependencyManager.addDependencyBlock(block);
    }

    public ControlHazardManager getControlHazardManager() {
        return controlHazardManager;
    }

    public StringBinary getREG_LO() {
        return REG_LO;
    }

    public void setREG_LO(StringBinary REG_LO) {
        this.REG_LO = REG_LO;
    }

    public StringBinary getREG_HI() {
        return REG_HI;
    }

    public void setREG_HI(StringBinary REG_HI) {
        this.REG_HI = REG_HI;
    }
}
