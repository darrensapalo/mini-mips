package dlsu.advcarc.cpurevised;

import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/30/2015.
 */
public abstract class AbstractEXStage extends  AbstractStage {

    protected StringBinary A;
    protected StringBinary B;
    protected StringBinary IMM;
    protected StringBinary ALUOutput = StringBinary.valueOf(0);

    protected int cond;
    protected StringBinary NPC;

    protected  AbstractEXStage(CPU cpu){
        super(cpu);
    }

    public abstract boolean hasCompletedExeuction();

    /* Getters */
    public int getCond(){
        return cond;
    }

    public StringBinary getALUOutput(){
        return ALUOutput;
    }

    public StringBinary getNPC(){
        return NPC;
    }

    public StringBinary getA() {
        return A;
    }

    public StringBinary getB() {
        return B;
    }

    public StringBinary getIMM() {
        return IMM;
    }

    @Override
    public void housekeeping(AbstractStage previousStage) {
        IDStage idStage = (IDStage) previousStage;
        A = idStage.getA();
        B = idStage.getB();
        IMM = idStage.getIMM();
        NPC = idStage.getNPC();
        this.IR = idStage.getIR();
        IRMemAddressHex = idStage.getIRMemAddressHex();
    }

    public JsonArray toJsonArray(String type) {
        return new JsonArray()
                .add(new JsonObject().put("register", "EX/MEM.B "+type).put("value", B.toHexString(16)))
                .add(new JsonObject().put("register", "EX/MEM.ALUOutput "+type).put("value", ALUOutput.toHexString(16)))
                .add(new JsonObject().put("register", "EX/MEM.Cond " +type).put("value", cond))
                .add(new JsonObject().put("register", "EX/MEM.IR "+type).put("value", IR.toHexString(16)));
    }


}


