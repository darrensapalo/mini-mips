package dlsu.advcarc.cpurevised;

import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.RegisterManager;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Created by user on 11/29/2015.
 */
public class IDStage extends AbstractStage{

    private StringBinary NPC;
    private StringBinary A;
    private StringBinary B;
    private StringBinary IMM;

    private boolean hasInstructionToForward;

    public IDStage(CPU cpu){
        super(cpu);
    }


    @Override
    public boolean hasInstructionToForward() {
        return hasInstructionToForward;
    }

    @Override
    protected boolean checkExtraDependenciesIfCanExecute() {
           /* Stall if there are Read After Write dependencies */
        List<String> registerReadDependencies = IR.getRegisterNamesToRead();
        for(String registerName: registerReadDependencies){
            if(cpu.hasPendingWrite(registerName))
                return false;
        }

        return true;
    }

    @Override
    public void execute() {
        /* Initialize A, B, and IMM */
        String aRegisterName = IR.getARegisterName();
        String bRegisterName = IR.getBRegisterName();

        A = aRegisterName == null ? StringBinary.valueOf(0) : RegisterManager.instance().getInstance(aRegisterName).getValue();
        B = bRegisterName == null ? StringBinary.valueOf(0) : RegisterManager.instance().getInstance(bRegisterName).getValue();
        IMM = IR.getImm();
    }

    @Override
    public JsonArray toJsonArray() {
        return new JsonArray()
                .add(new JsonObject().put("register", "ID/EX.A").put("value", A.toHexString(16)))
                .add(new JsonObject().put("register", "ID/EX.B").put("value",B.toHexString(16)))
                .add(new JsonObject().put("register", "ID/EX.Imm").put("value", IMM.toHexString(16)))
                .add(new JsonObject().put("register", "ID/EX.IR").put("value", IR.toHexString(16)))
                .add(new JsonObject().put("register", "ID/EX.NPC").put("value", NPC.toHexString(16)));
    }

    @Override
    public void housekeeping(AbstractStage previousStage) {
        IFStage ifStage = (IFStage) previousStage;
        IR = ifStage.getIR();
        NPC = ifStage.getNPC();
        IRMemAddressHex = ifStage.getIRMemAddressHex();
    }

    @Override
    public void resetRegisters() {
        A = StringBinary.valueOf(0);
        B = StringBinary.valueOf(0);
        IMM = StringBinary.valueOf(0);
        IR = Opcode.createNOP();
        NPC = StringBinary.valueOf(0);
    }

    public Opcode getIR() {
        return IR;
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

    public StringBinary getNPC() {
        return NPC;
    }

}
