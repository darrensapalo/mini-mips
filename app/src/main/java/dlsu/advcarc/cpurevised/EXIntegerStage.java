package dlsu.advcarc.cpurevised;

import dlsu.advcarc.cpu.ALU;
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
        return true;
    }

    @Override
    public void execute() {

        switch(IR.getInstruction()){

            case "DADDU":
                ALUOutput = A.plus(B); break;

            case "OR":
                A = A.padBinaryValueStringBinary(64);
                B = B.padBinaryValueStringBinary(64);
                ALUOutput = A.or(B); break;

            case "SLT":
                ALUOutput = (A.getAsLong() < B.getAsLong()) ? StringBinary.valueOf(1) : StringBinary.valueOf(0);
                ALUOutput = ALUOutput.padBinaryValueStringBinary(64); break;

            case "DMULT":
                StringBinary rawProduct =  A.times(B).padBinaryValueArithmeticStringBinary(128);
                StringBinary hi = rawProduct.substring(0, 63);
                StringBinary lo = rawProduct.substring(64, 127);
                cpu.setHI(hi);
                cpu.setLO(lo);
                break;

            case "DSLL":
                int shiftTimes = IMM.substring(5, 9).getAsInt();
                ALUOutput = A.shiftRight(-1*shiftTimes);
                break;

            case "LW":
            case "LWU":
            case "SW":
            case "L.S":
            case "S.S":
                A = A.padBinaryValueArithmeticStringBinary(64);
                IMM = IMM.padBinaryValueStringBinary(64);
                ALUOutput = A.plus(IMM).forceLengthStringBinary(64);
                break;

            case "BEQ":
                cond = A.getAsLong() == B.getAsLong() ? 1 : 0;
                cpu.setIfStageCanCheckCond(true);

                IMM = IMM.padBinaryValueArithmeticStringBinary(64);
                IMM = IMM.times(StringBinary.valueOf(4)).forceLengthStringBinary(64);
                ALUOutput = NPC.plus(IMM).forceLengthStringBinary(64);
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
