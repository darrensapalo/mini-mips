package dlsu.advcarc.cpurevised;

import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/29/2015.
 */
public class IFStage extends AbstractStage{

    private StringBinary NPC;
    private StringBinary PC;

    private boolean hasInstructionToForward;

    private StringBinary prevEXALUOutput;
    private StringBinary prevEXNPC;
    private int prevEXCond;

    public IFStage(CPU cpu){
        super(cpu);
        NPC = new StringBinary("0");
        PC = new StringBinary("0");

        prevEXALUOutput = StringBinary.valueOf(0);
        prevEXNPC = StringBinary.valueOf(0);
        prevEXCond = 0;
    }

    @Override
    public boolean hasInstructionToForward() {
        return hasInstructionToForward;
    }

    @Override
    protected boolean checkExtraDependenciesIfCanExecute() {
        IRMemAddressHex = PC.toHexString(4);
        Opcode nextOpcode =  new Opcode(new StringBinary(MemoryManager.instance().getInstance(IRMemAddressHex).getAsBinary()));
        if(nextOpcode.isNOP())
           return false;
        return true;
    }

    @Override
    public void execute() {

        IRMemAddressHex = PC.toHexString(4);
        IR = new Opcode(new StringBinary(MemoryManager.instance().getInstance(IRMemAddressHex).getAsBinary()));

        /* NPC/PC control */
        EXIntegerStage ex = cpu.getEXIntegerStage();

        if(cpu.isBranchRunning()){
            isStalling = true;
            NPC = PC.plus(StringBinary.valueOf(4));
        }
        else{

            if(cpu.ifStageCanCheckCond()){
                if(prevEXCond == 1)
                    NPC = prevEXALUOutput;
                else
                    NPC = prevEXNPC;

                // set back to false
                cpu.setIfStageCanCheckCond(false);
                isStalling = true; //stall/flush one last time
            }
            else{
                isStalling = false;
                NPC = PC.plus(StringBinary.valueOf(4));

                if ("BEQ".equals(IR.getInstruction()) || "J".equals(IR.getInstruction()))
                    cpu.setRunningBranch(IRMemAddressHex);
            }
        }

        PC = NPC.clone();

        prevEXALUOutput = ex.getALUOutput();
        prevEXCond = ex.getCond();
        prevEXNPC = ex.getNPC();

    }

    @Override
    public JsonArray toJsonArray() {
        return new JsonArray()
                .add(new JsonObject().put("register", "IF/ID.IR").put("value",  getIRString()))
                .add(new JsonObject().put("register", "IF/ID.NPC").put("value", NPC == null ? "null" : NPC.toHexString(16)))
                .add(new JsonObject().put("register", "PC").put("value", PC.toHexString(16)))
                ;
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
