package dlsu.advcarc.cpurevised;

import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;

/**
 * Created by user on 11/30/2015.
 */
public class EXMultiplier extends  AbstractEXStage {

    public static final int NUM_CYCLES_NEEDED = 8;
    private int cycleCount = 0;

    protected EXMultiplier(CPU cpu) {
        super(cpu);
    }

    @Override
    public boolean hasCompletedExeuction() {
        return cycleCount >= NUM_CYCLES_NEEDED;
    }


    @Override
    public boolean hasInstructionToForward() {
        return false; //unused
    }

    @Override
    protected boolean checkExtraDependenciesIfCanExecute() {
        return true;
    }

    @Override
    protected void execute() {
        cycleCount++;
        if(cycleCount == NUM_CYCLES_NEEDED)
            ALUOutput = StringBinary.valueOf(A.getAsFloat() * B.getAsFloat());
    }

    @Override
    public void resetRegisters() {
        A = StringBinary.valueOf(0);
        B = StringBinary.valueOf(0);
        IMM = StringBinary.valueOf(0);
        ALUOutput = StringBinary.valueOf(0);
        cond = 0;
        NPC = StringBinary.valueOf(0);
        cycleCount = 0;
    }

    @Override
    public JsonArray toJsonArray() {
        return super.toJsonArray("Multiplier");
    }


}
