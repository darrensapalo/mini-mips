package dlsu.advcarc.cpu.block;

import dlsu.advcarc.parser.Instruction;

/**
 * Created by Darren on 11/26/2015.
 */
public class ControlHazardManager {
    private Instruction branchInstruction;

    public void finish(){
        branchInstruction = null;
    }

    public void setBranchInstruction(Instruction instruction){
        if (instruction != null && instruction.getInstruction().contains("BEQ")){
            branchInstruction = instruction;
        }
    }

    public Instruction checkBranch(){
        return branchInstruction;
    }

    public Instruction getBranchInstruction() {
        if (branchInstruction != null && branchInstruction.getStage() != Instruction.Stage.DONE)
            return branchInstruction;

        branchInstruction = null;
        return null;
    }
}
