package dlsu.advcarc.cpu.stage;

/**
 * Created by Darren on 11/9/2015.
 */
public abstract class Stage {
    protected abstract void housekeeping();
    public abstract void execute();
}
