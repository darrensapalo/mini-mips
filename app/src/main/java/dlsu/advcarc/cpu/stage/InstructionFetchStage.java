package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.cpu.block.ControlHazardManager;
import dlsu.advcarc.cpu.block.DataDependencyManager;
import dlsu.advcarc.cpu.stage.ex.ExecuteStageInteger;
import dlsu.advcarc.cpu.stage.ex.ExecuteStageSwitch;
import dlsu.advcarc.dependency.DataDependencyException;
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
    private ProgramCode code;

    private dlsu.advcarc.memory.Memory IFID_IR;
    private StringBinary IFID_NPC;
    private ExecuteStageSwitch executeStage;
    private Instruction nextInstruction;
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
        System.out.println(PC.getAsLong());
        String memoryLocation = RadixHelper.convertLongToHexString(PC.getAsLong());
        IFID_IR = MemoryManager.instance().getInstance(memoryLocation);

        String lineOfCode = this.code.getCode(Integer.valueOf(memoryLocation, 16));

        if (lineOfCode == null)
            throw new NullPointerException("There is no more available program data to read.");

        // IF/ID.NPC, PC = (EX/MEM.Cond) ? EX/MEM.ALUOutput : PC + 4;
        boolean shouldBranch = ("1").equals(executeStage.getEXMEM_Cond());
        if (shouldBranch){
            executeStage.setEXMEM_Cond("0");
            IFID_NPC = executeStage.getEXMEM_ALUOutput();
            nextInstruction = null;
        }else{
            IFID_NPC = StringBinary.valueOf(PC.getAsLong() + 4);
        }

        // Get references to registers
        instruction = new Instruction(new StringBinary(IFID_IR.getAsBinary()), lineOfCode, ++cycle, PC.toHexString());
        instruction.setStage(Instruction.Stage.IF);

        if (instruction != null && shouldBranch == false)
            nextInstruction = instruction;

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

    @Override
    public boolean canStageRun(DataDependencyManager dataDependencyManager) throws Exception {
        didRun = false;

        Instruction instruction = getInstruction();
        if (instruction != null) {

            ControlHazardManager controlHazardManager = cpu.getControlHazardManager();
            Instruction branch = controlHazardManager.checkBranch();


            DataDependencyException dependency = instruction.getDependencyWithBlock();

            if (dependency != null) {
                DataDependencyManager.DataDependencyBlock block = dependency.getDataDependencyBlock();

                if (block != null) {
                    Instruction ownedBy = block.getOwnedBy();

                    // If the instruction is less than or equal to WB, block still. It needs to be beyond it.
                    if (ownedBy.getStage().ordinal() <= block.getReleaseStage().ordinal())
                        throw new Exception("Cannot run " + this.getClass().getSimpleName() + " because there is currently a data dependency block.");
                }
            }
        }

        if (this.instruction == null) return true;

        return super.canStageRun(dataDependencyManager);
    }

    public JsonArray toJsonArray() {
        return new JsonArray()
                .add(new JsonObject().put("register", "IF/ID.IR").put("value", IFID_IR == null ? "null" : IFID_IR.getAsHex()))
                .add(new JsonObject().put("register", "IF/ID.NPC").put("value", IFID_NPC == null ? "null" : IFID_NPC.toHexString()));
    }

    public void reset() {
        instruction = null;
        IFID_IR = null;
    }

    public Instruction getNextInstruction() {
        return nextInstruction;
    }
}
