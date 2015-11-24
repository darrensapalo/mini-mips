package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.parser.Instruction;

/**
 * Created by Darren on 11/9/2015.
 */
public abstract class Stage {
    protected boolean isBlocked;
    protected int stageId;
    protected Instruction instruction;
    protected boolean didRun;

    public abstract void housekeeping();

    public abstract void execute();

    public Instruction getInstruction(){
        return instruction;
    }

    public boolean isBlocked() {
        return isBlocked;
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
}
