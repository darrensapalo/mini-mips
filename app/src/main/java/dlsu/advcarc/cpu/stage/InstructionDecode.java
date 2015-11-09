package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;

/**
 * Created by Darren on 11/9/2015.
 */
public class InstructionDecode extends Stage {
    private CPU cpu;
    private InstructionFetch instructionFetchStage;

    public InstructionDecode(CPU cpu, InstructionFetch instructionFetchStage) {
        this.cpu = cpu;
        this.instructionFetchStage = instructionFetchStage;
    }

    @Override
    protected void housekeeping() {

    }

    @Override
    public void execute() {
        // instructionFetchStage.get code, get from registers
    }
}
