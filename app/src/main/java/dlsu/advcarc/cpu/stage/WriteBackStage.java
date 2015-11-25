package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.cpu.block.DataDependencyManager;
import dlsu.advcarc.dependency.DataDependencyException;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;

/**
 * Created by Darren on 11/9/2015.
 */
public class WriteBackStage extends Stage {
    private CPU cpu;
    private MemoryStage memoryStage;
    private Memory LMD;
    private Memory IR;
    private StringBinary ALUOutput;


    public WriteBackStage(CPU cpu, MemoryStage memoryStage) {
        this.cpu = cpu;
        this.memoryStage = memoryStage;
        stageId = 4;
    }

    @Override
    public void housekeeping() {
        try {

                instruction = memoryStage.getInstruction();

                LMD = memoryStage.getMEMWB_LMD();
                ALUOutput = memoryStage.getMEMWB_ALUOutput();
                IR = memoryStage.getMEMWB_IR();
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage());
        }
    }

    @Override
    public void execute() {

        didRun = false;
        Parameter source, destination;

        instruction.analyzeDependencies();
        instruction.checkDependencies();
        instruction.addDependencies();

        String instruction = this.instruction.getInstructionOnly();
        switch (instruction) {
            // J Types
            case "J":
                break;

            // R Types
            case "DADDU":
            case "OR":
            case "SLT":
            case "DSLL":
                destination = this.instruction.getParameters().get(2);
                destination.getParameter().write(ALUOutput.getBinaryValue());
                break;

            case "DMULT":

                // todo: write to HI LO
                break;


            // Rx Types
            case "ADD.S":
                break;
            case "MUL.S":
                break;

            // I Types
            case "LW":
            case "LWU":
            case "L.S":
            case "S.S":
            case "SW":
                break;

            case "BEQ":
                break;


            default:
                break;
        }


        // todo: dequeue dependencies


        DataDependencyException dependencyWithBlock = this.instruction.getDependencyWithBlock();
        if (dependencyWithBlock != null) {
            DataDependencyManager.DataDependencyBlock block = dependencyWithBlock.getDataDependencyBlock();
            if (block != null) {
                if (block.getReleaseStage() == Instruction.Stage.WB)
                    cpu.reviewBlock(this.instruction);
            }
        }


        didRun = true;
    }

}
