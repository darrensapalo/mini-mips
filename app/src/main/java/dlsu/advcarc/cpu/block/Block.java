package dlsu.advcarc.cpu.block;

import dlsu.advcarc.cpu.stage.*;
import dlsu.advcarc.cpu.stage.ex.ExecuteStageInteger;
import dlsu.advcarc.parser.Instruction;

/**
 * Created by Darren on 11/22/2015.
 */
public class Block {

    private final Instruction.Stage releaseStage;
    private final Instruction.Stage blockStage;
    private final Instruction ownedBy;

    public Block(Instruction ownedBy, Instruction.Stage releaseStage, Instruction.Stage blockStage) {
        this.releaseStage = releaseStage;
        this.blockStage = blockStage;
        this.ownedBy = ownedBy;
    }

    public Instruction.Stage getReleaseStage() {
        return releaseStage;
    }

    public int getBlockStage() {
        if (blockStage == null)
            return -1;
        return blockStage.ordinal();
    }

    public Instruction getOwnedBy() {
        return ownedBy;
    }

    public boolean canStageRun(Stage stage, Stage previous) throws Exception {
        if (stage instanceof InstructionFetchStage == false && stage.getInstruction() == null)
            throw new Exception("NOP");

        if (previous != null && previous.isBlocked())
            throw new Exception("Cannot run " + stage.getClass().getSimpleName() + " because previous stage is blocked");

        if (ownedBy != null && ownedBy.equals(stage.getInstruction()) == false)
            throw new Exception("Cannot run " + stage.getClass().getSimpleName() + " because the instruction of this stage is not the owner of the data dependency block.");

        Instruction instruction = stage.getInstruction();
        if (stage instanceof InstructionFetchStage == false && instruction != null && instruction.getStage().ordinal() != stage.getStageId())
            throw new Exception("Cannot run " + stage.getClass().getSimpleName() + " because the instruction of this stage has been passed, but is not yet at this stage.");

        if (stage instanceof InstructionFetchStage && blockStage == Instruction.Stage.ID)
            throw new Exception("Cannot run " + stage.getClass().getSimpleName() + " because there is currently a data dependency block.");

        return true;
    }

    public static Block none() {
        return new Block(null, null, null);
    }

    public boolean canHousekeepingRun(Stage stage, Stage previous) {
        boolean allowed;
        try {


            if (blockStage == null) return true;

            int stageNumber = -1;
            if (stage instanceof InstructionFetchStage)
                stageNumber = 0;
            else if (stage instanceof InstructionDecodeStage)
                stageNumber = 1;
            else if (stage instanceof ExecuteStageInteger)
                stageNumber = 2;
            else if (stage instanceof MemoryStage)
                stageNumber = 3;
            else if (stage instanceof WriteBackStage)
                stageNumber = 4;


            Instruction instruction = previous.getInstruction();
            allowed = ownedBy.equals(instruction);


            return allowed && stageNumber > blockStage.ordinal();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
