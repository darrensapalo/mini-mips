package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.Register;

import java.util.ArrayList;

/**
 * Created by Darren on 11/9/2015.
 */
public class WriteBackStage extends Stage {
    private CPU cpu;
    private MemoryStage memoryStage;
    private Memory LMD;
    private Memory IR;
    private StringBinary ALUOutput;
    private Instruction instruction;


    public WriteBackStage(CPU cpu, MemoryStage memoryStage) {
        this.cpu = cpu;
        this.memoryStage = memoryStage;
    }

    @Override
    public void housekeeping() {
        LMD = memoryStage.getMEMWB_LMD();
        ALUOutput = memoryStage.getMEMWB_ALUOutput();
        IR = memoryStage.getMEMWB_IR();
        instruction = memoryStage.getInstruction();
    }

    @Override
    public void execute() {
        Parameter source, destination;


        ArrayList<Parameter> parameters = this.instruction.getParameters();

        // If I have dependencies, block
        for (Parameter p : parameters) {
            if (p.getParameter() instanceof Register) {
                Instruction dependentOnThis = p.peekDependency();
                if (this.instruction.equals(dependentOnThis) == false) {
                    cpu.setDataDependencyBlock(4);
                    return;
                }
            }
        }


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
            case "S.S":
            case "SW":
                break;

            case "BEQ":
                break;



            default:
                break;
        }


        // todo: dequeue dependencies

        // If I have dependencies, block
        for (Parameter p : parameters) {
            if (p.getParameter() instanceof Register) {
                p.dequeueDependency();
            }
        }
    }
}
