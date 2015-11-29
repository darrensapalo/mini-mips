package dlsu.advcarc.cpurevised;

import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/29/2015.
 */
public class IFStage extends AbstractStage{

    private StringBinary NPC;
    private StringBinary PC;

    private boolean hasInstructionToForward;

    public IFStage(CPU cpu){
        super(cpu);
        NPC = new StringBinary("0");
        PC = new StringBinary("0");
    }

    @Override
    public boolean hasInstructionToForward() {
        return hasInstructionToForward;
    }

    @Override
    protected boolean checkExtraDependenciesIfCanExecute() {
        IRMemAddressHex = PC.toHexString(4);
        Opcode nextOpcode =  new Opcode(new StringBinary(MemoryManager.instance().getInstance(IRMemAddressHex).getAsBinary()));
        return !nextOpcode.isNOP();
    }

    @Override
    public void execute() {
        hasInstructionToForward = false;

        IRMemAddressHex = PC.toHexString(4);
        IR = new Opcode(new StringBinary(MemoryManager.instance().getInstance(IRMemAddressHex).getAsBinary()));

        /* NPC/PC control */
        EXIntegerStage ex = cpu.getEXIntegerStage();

        if(ex.getCond() == 1){
            NPC = ex.getALUOutput();
        }
        else{ // Cond == 0

            // If branch ex has just finished and no jump/branch is taken, go back to the instruction after J/BEQ
            if(cpu.justFinishedBranchEx())
                NPC = ex.getNPC();
            else
                NPC = PC.plus(StringBinary.valueOf(4));
        }

        PC = NPC.clone();

        if(cpu.justFinishedBranchEx())
            cpu.setJustFinishedBranchEx(false);

        if("BEQ".equals(IR.getInstruction()) || "J".equals(IR.getInstruction()))
            cpu.setRunningBranch(IRMemAddressHex);

        hasInstructionToForward = true;
    }

    @Override
    public JsonArray toJsonArray() {
        return new JsonArray()
                .add(new JsonObject().put("register", "IF/ID.IR").put("value",  getIRString()))
                .add(new JsonObject().put("register", "IF/ID.NPC").put("value", NPC == null ? "null" : NPC.toHexString(16)));
    }

    @Override
    public void housekeeping(AbstractStage previousStage) {

    }

    @Override
    public void resetRegisters() {
    }

    public boolean isNOP(){
        return IR == null || "NOP".equals(IR.getInstruction());
    }

    public Opcode getIR() {
        return IR;
    }

    public StringBinary getNPC() {
        return NPC;
    }

}
