package dlsu.advcarc.cpurevised;

import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;

/**
 * Created by user on 11/29/2015.
 */
public class EXSwitch implements CPUStage {

    private EXIntegerStage integer;

    @Override
    public boolean hasInstructionToForward() {
        return integer.hasInstructionToForward();
    }

    @Override
    public void execute() {
        integer.execute();
        //TODO add the adder and multiplier

        //TODO add dependency checking for Write after Write (only applies to S.S after ADD.S or MULT.S)
    }

    @Override
    public JsonArray toJsonArray() {
        return null;
    }

    @Override
    public void housekeeping(CPUStage previousStage) {
        //TODO select appropriate EX component
        integer.housekeeping(previousStage);
    }

    public Opcode getIR(){
        //TODO select appropriate EX component
        return integer.getIR();
    }

    public StringBinary getB(){
        //TODO select appropriate EX component
        return integer.getB();
    }

    public StringBinary getALUOutput(){
        //TODO select appropriate EX component
        return integer.getALUOutput();
    }

}
