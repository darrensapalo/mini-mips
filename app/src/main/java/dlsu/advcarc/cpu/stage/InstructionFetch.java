package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.parser.ProgramCode;

/**
 * Created by Darren on 11/9/2015.
 */
public class InstructionFetch extends Stage {

    private CPU cpu;
    private ProgramCode code;

    private String fetchedLine;

    public InstructionFetch(CPU cpu, ProgramCode code) {
        this.cpu = cpu;
        this.code = code;
    }

    @Override
    protected void housekeeping() {

    }

    @Override
    public void execute() {
        int programCounter = cpu.getProgramCounter();
        fetchedLine = code.getCode(programCounter);
    }
}
