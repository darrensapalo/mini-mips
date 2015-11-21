package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.opcode.OpcodeHelper;
import dlsu.advcarc.parser.StringBinary;

/**
 * Created by Darren on 11/9/2015.
 */
public class MemoryStage extends Stage {
    private ExecuteStage executeStage;

    private StringBinary MEMWB_ALUOutput;
    private Memory MEMWB_LMD;
    private dlsu.advcarc.memory.Memory MEMWB_IR;

    public MemoryStage(ExecuteStage executeStage) {
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

        String instruction = OpcodeHelper.getInstruction(new StringBinary(MEMWB_IR.getAsBinary()));
        switch (instruction){
            case "LW":
            case "LWU":
            case "SW":
            case "L.S":
            case "S.S":
                MEMWB_LMD = MemoryManager.instance().getInstance(MEMWB_ALUOutput.toHexString());
                break;
        }

        // todo: perform branch completion
    }

    public StringBinary getMEMWB_ALUOutput() {
        return MEMWB_ALUOutput;
    }

    public Memory getMEMWB_LMD() {
        return MEMWB_LMD;
    }

    public Memory getMEMWB_IR() {
        return MEMWB_IR;
    }
}
