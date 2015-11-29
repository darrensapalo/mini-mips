package dlsu.advcarc.cpurevised;

import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.RegisterManager;
import io.vertx.core.json.JsonArray;

import java.util.List;

/**
 * Created by user on 11/29/2015.
 */
public class IDStage implements  CPUStage{

    private Opcode IR;
    private CPU cpu;
    private StringBinary NPC;

    private StringBinary A;
    private StringBinary B;
    private StringBinary IMM;



    private boolean hasInstructionToForward;

    public IDStage(CPU cpu){
        this.cpu = cpu;
    }


    @Override
    public boolean hasInstructionToForward() {
        return false;
    }

    @Override
    public void execute() {

        if(IR == null) {
            hasInstructionToForward = false;
            return;
        }


        if(cpu.isFlushing() && !IR.isBranchOrJump()) // TODO || nexxt stage is stalling
        {
            hasInstructionToForward = false;
            return;
        }

        /* Stall if there are Read After Write dependencies */
        List<String> registerReadDependencies = IR.getRegisterNamesToRead();
        for(String registerName: registerReadDependencies){
            if(cpu.hasPendingWrite(registerName)) {
                hasInstructionToForward = false;
                return;
            }
        }


        /* Initialize A, B, and IMM */
        String aRegisterName = IR.getARegisterName();
        String bRegisterName = IR.getBRegisterName();

        A = aRegisterName == null ? StringBinary.valueOf(0) : RegisterManager.instance().getInstance(aRegisterName).getValue();
        B = bRegisterName == null ? StringBinary.valueOf(0) : RegisterManager.instance().getInstance(bRegisterName).getValue();
        IMM = IR.getImm();
    }

    @Override
    public JsonArray toJsonArray() {
        return null;
    }

    @Override
    public void housekeeping(CPUStage previousStage) {
        IFStage ifStage = (IFStage) previousStage;
        IR = ifStage.getIR();
        NPC = ifStage.getNPC();
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
