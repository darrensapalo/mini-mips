package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;

/**
 * Created by Darren on 11/9/2015.
 */
public class Execute extends Stage {
    private final CPU cpu;
    private InstructionDecode instructionDecodeStage;

    public Execute(CPU cpu, InstructionDecode instructionDecodeStage) {
        this.cpu = cpu;
        this.instructionDecodeStage = instructionDecodeStage;
    }

    @Override
    protected void housekeeping() {

    }

    @Override
    public void execute() {

    }
}
