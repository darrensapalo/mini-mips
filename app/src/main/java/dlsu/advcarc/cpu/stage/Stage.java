package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.block.DataDependencyManager;
import dlsu.advcarc.cpu.stage.ex.ExecuteStageAdder;
import dlsu.advcarc.cpu.stage.ex.ExecuteStageInteger;
import dlsu.advcarc.cpu.stage.ex.ExecuteStageMultiplier;
import dlsu.advcarc.cpu.stage.ex.ExecuteStageSwitch;
import dlsu.advcarc.dependency.DataDependencyException;
import dlsu.advcarc.parser.Instruction;

/**
 * Created by Darren on 11/9/2015.
 */
public abstract class Stage {
    protected int stageId;
    protected Instruction instruction;
    protected boolean didRun;

    /**
     * The house keeping process is the part where the stage gets the values from the previous stage.
     */
    public abstract void housekeeping();

    /**
     * The execute process is the part where the stage performs all the necessary executions of the current stage.
     */
    public abstract void execute();

    public Instruction getInstruction() {
        return instruction;
    }

    public void incrementInstruction() {
        Instruction instruction = getInstruction();
        try {
            if (didRun && instruction != null) {
                Instruction.Stage[] values = Instruction.Stage.values();

                if (instruction.getStage().ordinal() == stageId) {
                    instruction.setStage(values[stageId + 1]);

                    Instruction.Stage stage = instruction.getStage();
                    if (stage != null)
                        System.out.println(instruction + " was delivered to stage " + stage.toString());

                    if (stage == Instruction.Stage.DONE)
                        this.instruction = null;


                }
            }
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        }
    }

    public int getStageId() {
        return stageId;
    }

    public boolean didRun() {
        return didRun;
    }


    public boolean canStageRun(DataDependencyManager dataDependencyManager) throws Exception {
        if (!(this instanceof InstructionFetchStage) && this.getInstruction() == null)
            throw new Exception("NOP");

        Instruction instruction = this.getInstruction();
        if (!(this instanceof InstructionFetchStage) && instruction != null && instruction.getStage().ordinal() != this.getStageId())
            throw new Exception("Cannot run " + this.getClass().getSimpleName() + " because the instruction of this stage " + instruction + " has been passed, but is not yet at this stage. It is currently in stage " + instruction.getStage() + ".");


        return true;
    }


    public boolean canHousekeepingRun(Stage previous, DataDependencyManager dataDependencyManager) {
        try {
            // Previous stage's instruction data
            Instruction previousInstruction = previous instanceof InstructionFetchStage ? ((InstructionFetchStage) previous).getNextInstruction() : previous.getInstruction();

            DataDependencyException dataDependencyException = previousInstruction.getDependencyWithBlock();

            if (dataDependencyException != null) {
                DataDependencyManager.DataDependencyBlock dataDependencyBlock = dataDependencyException.getDataDependencyBlock();
                if (dataDependencyBlock != null) {

                    // data dependency block data
                    Instruction.Stage releaseStage = dataDependencyBlock.getReleaseStage();

                    if (releaseStage != null && dataDependencyException != null) {
                        Instruction instruction = dataDependencyException.getDependentOnThis();

                        boolean instructionIsPastReleaseStage = dataDependencyBlock.getReleaseStage().ordinal() < dataDependencyBlock.getOwnedBy().getStage().ordinal();

                        boolean hazardTypeIsWriteAfterWrite = dataDependencyBlock.getDataHazardType() == DataDependencyManager.DataHazardType.WriteAfterWrite;

                        boolean previousInstructionIsTheSameAsTheDependency = previousInstruction.equals(instruction);

                        if (previousInstructionIsTheSameAsTheDependency)
                            throw new Exception("Pointless to run housekeeping because the previous instruction is the same as the current instruction " + instruction);

                        if (!instructionIsPastReleaseStage && !hazardTypeIsWriteAfterWrite)
                            throw new Exception("Cannot run housekeeping on stage " + getClass().getSimpleName() + " because the previous instruction " + previousInstruction + " is dependent on " + instruction + " which releases the lock at stage " + releaseStage);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage());
        }
        return false;
    }

    public int getStageNumber() {
        return getStageNumber(this);
    }

    public static int getStageNumber(Stage stage) {
        if (stage instanceof InstructionFetchStage)
            return 0;
        if (stage instanceof InstructionDecodeStage)
            return 1;
        if (stage instanceof ExecuteStageSwitch || stage instanceof ExecuteStageInteger || stage instanceof ExecuteStageAdder || stage instanceof ExecuteStageMultiplier)
            return 2;
        if (stage instanceof MemoryStage)
            return 3;
        if (stage instanceof WriteBackStage)
            return 4;
        return -1;
    }

    public static int getStageNumber(Instruction.Stage stage) {
        return stage.ordinal();
    }
}
