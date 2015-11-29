package dlsu.advcarc.cpurevised;

import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/29/2015.
 */
public class EXIntegerStage extends AbstractStage{

    private StringBinary A;
    private StringBinary B;
    private StringBinary IMM;
    private StringBinary ALUOutput;

    private int cond;
    private StringBinary NPC;

    public EXIntegerStage(CPU cpu){
        super(cpu);
        resetRegisters();
    }

    @Override
    public boolean hasInstructionToForward() {
        return !IR.isNOP();
    }

    @Override
    protected boolean checkExtraDependenciesIfCanExecute() {
        return true;
    }

    @Override
    public void execute() {

        switch(IR.getInstruction()){

            case "DADDU":
                ALUOutput = A.plus(B); break;

            //TODO implement the rest here:

            case "BEQ":
            case "J":
                cpu.setRunningBranch(null);
                break;
        }

    }

    @Override
    public JsonArray toJsonArray() {
        return new JsonArray()
                .add(new JsonObject().put("register", "EX/MEM.B").put("value", B.toHexString(16)))
                .add(new JsonObject().put("register", "EX/MEM.ALUOutput").put("value", ALUOutput.toHexString(16)))
                .add(new JsonObject().put("register", "EX/MEM.Cond").put("value", cond))
                .add(new JsonObject().put("register", "EX/MEM.IR").put("value", IR.toHexString(16)));
    }

    @Override
    public void housekeeping(AbstractStage previousStage) {
        IDStage idStage = (IDStage) previousStage;
        A = idStage.getA();
        B = idStage.getB();
        IMM = idStage.getIMM();
        NPC = idStage.getNPC();
        IR = idStage.getIR();
        IRMemAddressHex = idStage.getIRMemAddressHex();
    }

    @Override
    public void resetRegisters() {
        A = StringBinary.valueOf(0);
        B = StringBinary.valueOf(0);
        IMM = StringBinary.valueOf(0);
        ALUOutput = StringBinary.valueOf(0);
    }

    public int getCond(){
        return 0; //TODO
    }

    public StringBinary getALUOutput(){
        return ALUOutput;
    }

    public StringBinary getNPC(){
        return StringBinary.valueOf(0); //TODO
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

}
