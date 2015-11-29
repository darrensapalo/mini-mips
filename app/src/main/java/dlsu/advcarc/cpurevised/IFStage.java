package dlsu.advcarc.cpurevised;

import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/29/2015.
 */
public class IFStage implements CPUStage{


    private CPU cpu;
    private Opcode IR;

    private StringBinary NPC;
    private StringBinary PC;

    private boolean hasInstructionToForward;


    public IFStage(CPU cpu){
        this.cpu = cpu;
        NPC = new StringBinary("0");
        PC = new StringBinary("0");
    }

    @Override
    public boolean hasInstructionToForward() {
        return hasInstructionToForward;
    }

    @Override
    public void execute() {
        IR = new Opcode(new StringBinary(MemoryManager.instance().getInstance(PC.toHexString(4)).getAsBinary()));

        if("NOP".equals(IR.getInstruction()))
            return;

        /* NPC/PC control */
        EXIntegerStage ex = cpu.getEXIntegerStage();

        if(ex.getCond() == 1){
            NPC = ex.getALUOutput();
            cpu.setFlushing(false);
        }
        else{ // Cond == 0

            // If branch ex has just finished and no jump/branch is taken, go back to the instruction after J/BEQ
            if(cpu.justFinishedBranchEx()) {
                NPC = ex.getNPC();
                cpu.setFlushing(false);
                cpu.setJustFinishedBranchEx(false);
            }
            else
                NPC = PC.plus(StringBinary.valueOf(4));
        }

    }

    @Override
    public JsonArray toJsonArray() {
        return new JsonArray()
                .add(new JsonObject().put("register", "IF/ID.IR").put("value", IR == null ? "null" : IR.toHexString()))
                .add(new JsonObject().put("register", "IF/ID.NPC").put("value", NPC == null ? "null" : NPC.toHexString()));
    }

    @Override
    public void housekeeping(CPUStage previousStage) {

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
