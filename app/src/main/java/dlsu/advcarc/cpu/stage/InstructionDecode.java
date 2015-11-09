package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;

import java.util.ArrayList;

/**
 * Created by Darren on 11/9/2015.
 */
public class InstructionDecode extends Stage {
    private CPU cpu;
    private InstructionFetch instructionFetchStage;
    private Instruction instruction;

    public InstructionDecode(CPU cpu, InstructionFetch instructionFetchStage) {
        this.cpu = cpu;
        this.instructionFetchStage = instructionFetchStage;
    }

    @Override
    protected void housekeeping() {

    }

    @Override
    public void execute() {
        // instructionFetchStage.get code
        String fetchedLine = instructionFetchStage.getFetchedLine();

        // Get references to registers
        instruction = new Instruction(fetchedLine);

        ArrayList<Parameter> parameters = instruction.getParameters();

        for (Parameter p : parameters) {
            p.analyzeDependency();
        }


    }
}
