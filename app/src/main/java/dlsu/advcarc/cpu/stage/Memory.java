package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;

/**
 * Created by Darren on 11/9/2015.
 */
public class Memory extends Stage {
    private CPU cpu;
    private Execute executeStage;

    public Memory(CPU cpu, Execute executeStage) {
        this.cpu = cpu;
        this.executeStage = executeStage;
    }

    @Override
    protected void housekeeping() {

    }

    @Override
    public void execute() {

    }
}
