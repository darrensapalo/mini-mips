package dlsu.advcarc.cpurevised;

import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;

/**
 * Created by user on 11/29/2015.
 */
public class EXIntegerStage implements CPUStage {


    private Opcode IR;
    private StringBinary A;
    private StringBinary B;
    private StringBinary IMM;

    private StringBinary ALUOutput;

    private int cond;
    private StringBinary NPC;
    private boolean hasFinishedExecuting;

    @Override
    public boolean hasInstructionToForward() {
        return false;
    }

    @Override
    public void execute() {

        switch(IR.getInstruction()){

            case "DADDU":
                ALUOutput = A.plus(B); break;

            //TODO implement the rest here:

        }

        hasFinishedExecuting = true;
    }

    @Override
    public JsonArray toJsonArray() {
        return null;
    }

    @Override
    public void housekeeping(CPUStage previousStage) {
        IDStage idStage = (IDStage) previousStage;
        A = idStage.getA();
        B = idStage.getB();
        IMM = idStage.getIMM();
        NPC = idStage.getNPC();
    }

    public int getCond(){
        return 0; //TODO
    }

    public StringBinary getALUOutput(){
        return StringBinary.valueOf(0); //TODO
    }

    public StringBinary getNPC(){
        return StringBinary.valueOf(0); //TODO
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

    public boolean isHasFinishedExecuting() {
        return hasFinishedExecuting;
    }
}
