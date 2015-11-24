package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.cpu.stage.ex.ExecuteStageInteger;
import dlsu.advcarc.cpu.stage.ex.ExecuteStageSwitch;
import dlsu.advcarc.memory.*;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Darren on 11/9/2015.
 */
public class InstructionFetchStage extends Stage {

    private CPU cpu;
    private ProgramCode code;

    private dlsu.advcarc.memory.Memory IFID_IR;
    private StringBinary IFID_NPC;
    private ExecuteStageSwitch executeStage;
    private int cycle;

    public InstructionFetchStage(CPU cpu, ProgramCode code) {
        this.cpu = cpu;
        this.code = code;
        stageId = 0;
        cycle = 0;
    }

    @Override
    public void housekeeping() {

    }

    @Override
    public void execute() {
        didRun = false;
        StringBinary PC = cpu.getProgramCounter();

        // IF/ID.IR = Mem[PC]
        String memoryLocation = RadixHelper.convertLongToHexString(PC.getAsLong());
        IFID_IR = MemoryManager.instance().getInstance(memoryLocation);

        String lineOfCode = this.code.getCode(Integer.valueOf(memoryLocation, 16));

        if (lineOfCode == null)
            throw new NullPointerException("There is no more available program data to read.");

        // IF/ID.NPC, PC = (EX/MEM.Cond) ? EX/MEM.ALUOutput : PC + 4;
        IFID_NPC = ("1").equals(executeStage.getEXMEM_Cond()) ? executeStage.getEXMEM_ALUOutput() : StringBinary.valueOf(PC.getAsLong() + 4);

        // Get references to registers
        instruction = new Instruction(new StringBinary(IFID_IR.getAsBinary()), lineOfCode, ++cycle);
        instruction.setStage(Instruction.Stage.IF);
        System.out.println("IF Stage: Read a new instruction from program code - " + instruction.toString());

        PC = IFID_NPC;
        cpu.setProgramCounter(PC);
        didRun = true;
    }

    public void setExecuteStage(ExecuteStageSwitch executeStage) {
        this.executeStage = executeStage;
    }

    public Memory getIFID_IR() {
        return IFID_IR;
    }

    public StringBinary getIFID_NPC() {
        return IFID_NPC;
    }


    public JsonArray toJsonArray(){
        return new JsonArray()
                .add(new JsonObject().put("register", "IF/ID.IR").put("value", IFID_IR == null? "null": IFID_IR.getAsHex()))
                .add(new JsonObject().put("register","IF/ID.NPC").put("value", IFID_NPC == null ? "null" : IFID_NPC.toHexString()));
    }

    public void reset(){
        instruction = null;
        IFID_IR = null;
    }
}
