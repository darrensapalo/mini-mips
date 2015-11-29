package dlsu.advcarc.cpurevised;

/**
 * Created by user on 11/29/2015.
 */
public interface CPUStage {

    public boolean hasInstructionToForward();

    public void execute();


}
