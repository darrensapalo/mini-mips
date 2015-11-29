package dlsu.advcarc.cpurevised;

import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/29/2015.
 */
public class EXIntegerStage extends AbstractEXStage{

    public EXIntegerStage(CPU cpu){
        super(cpu);
        resetRegisters();
    }

    @Override
    public boolean hasCompletedExeuction() {
        return true;
    }

    @Override
    public boolean hasInstructionToForward() {
        return !IR.isNOP();
    }

    @Override
    protected boolean checkExtraDependenciesIfCanExecute() {
        return true;// TODO if L.S check if there is an active ADD.S or MULT.S
    }

    @Override
    public void execute() {

        switch(IR.getInstruction()){

            case "DADDU":
                ALUOutput = A.plus(B); break;

            //TODO implement the rest here:

            case "BEQ":
                cpu.setIfStageCanCheckCond(true);
                ALUOutput = A.padBinaryValueArithmeticStringBinary(64).plus(IMM.padBinaryValueArithmeticStringBinary(64));
                break;
            case "J":
                cpu.setIfStageCanCheckCond(true);
                cond = 1;
                ALUOutput = IR.getSubBinary(6,31).times(StringBinary.valueOf(4)).padBinaryValueStringBinary(64);
                break;
        }

    }


    @Override
    public void resetRegisters() {
        A = StringBinary.valueOf(0);
        B = StringBinary.valueOf(0);
        IMM = StringBinary.valueOf(0);
        ALUOutput = StringBinary.valueOf(0);
        cond = 0;
        NPC = StringBinary.valueOf(0);
    }

    @Override
    public JsonArray toJsonArray() {
        return super.toJsonArray("");
    }
}
