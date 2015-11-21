package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.opcode.OpcodeHelper;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;

/**
 * Created by Darren on 11/9/2015.
 */
public class WriteBackStage extends Stage {
    private MemoryStage memoryStage;
    private Memory LMD;
    private Memory IR;
    private StringBinary ALUOutput;


    public WriteBackStage(MemoryStage memoryStage) {
        this.memoryStage = memoryStage;
    }

    @Override
    protected void housekeeping() {
        LMD = memoryStage.getMEMWB_LMD();
        ALUOutput = memoryStage.getMEMWB_ALUOutput();
        IR = memoryStage.getMEMWB_IR();
    }

    @Override
    public void execute() {
        housekeeping();

        Parameter source, destination;
        Instruction instruction = new Instruction(IR.getAsBinary());
        switch (instruction.getInstruction()) {
            // J Types
            case "J":
                break;

            // R Types
            case "DADDU":
            case "OR":
            case "SLT":
            case "DSLL":
                destination = instruction.getParameters().get(2);
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
                destination = instruction.getParameters().get(1);
                destination.getParameter().write(LMD.getAsBinary());
                break;

            case "S.S":
            case "SW":
                source = instruction.getParameters().get(1);
                LMD.write(source.getParameter().read());
                break;

            case "BEQ":
                break;



            default:
                break;
        }


        // todo: dequeue dependencies
    }
}
