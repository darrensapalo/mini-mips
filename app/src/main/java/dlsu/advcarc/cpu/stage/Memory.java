package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.parser.StringBinary;

/**
 * Created by Darren on 11/9/2015.
 */
public class Memory extends Stage {
    private CPU cpu;
    private Execute executeStage;

    private StringBinary MEMWB_ALUOutput;
    private dlsu.advcarc.memory.Memory MEMWB_IR;

    public Memory(CPU cpu, Execute executeStage) {
        this.cpu = cpu;
        this.executeStage = executeStage;
    }

    @Override
    protected void housekeeping() {

        // ALU instructions
        MEMWB_IR = executeStage.getEXMEM_IR();
        MEMWB_ALUOutput = executeStage.getEXMEM_ALUOutput();
    }

    @Override
    public void execute() {
        housekeeping();


        // if Load store instructions

    }

    public StringBinary getMEMWB_ALUOutput() {
        return MEMWB_ALUOutput;
    }

    public dlsu.advcarc.memory.Memory getMEMWB_IR() {
        return MEMWB_IR;
    }
}
