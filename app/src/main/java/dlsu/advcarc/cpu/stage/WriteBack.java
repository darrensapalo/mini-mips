package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;

/**
 * Created by Darren on 11/9/2015.
 */
public class WriteBack extends Stage {
    private CPU cpu;

    public WriteBack(CPU cpu) {

        this.cpu = cpu;
    }

    @Override
    protected void housekeeping() {

    }

    @Override
    public void execute() {

    }
}
