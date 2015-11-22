package dlsu.advcarc.cpu.block;

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

    public static Block none(){
        return new Block(null, null, null);
    }
}
