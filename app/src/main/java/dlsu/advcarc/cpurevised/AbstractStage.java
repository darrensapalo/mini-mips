package dlsu.advcarc.cpurevised;

import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;

/**
 * Created by user on 11/29/2015.
 */
public abstract class AbstractStage {

    protected Opcode IR;
    protected String IRMemAddressHex;

    protected AbstractStage(){
        resetToNOP();
    }

    public abstract boolean hasInstructionToForward();

    public  boolean isReadyToAcceptInstruction(){
        return "NOP".equals(IR.getInstruction());
    }

    public abstract boolean execute();

    public abstract void housekeeping(AbstractStage previousStage);

    public void resetToNOP(){
        IR = Opcode.createNOP();
        IRMemAddressHex = StringBinary.valueOf(0).toHexString();
        resetRegisters();
    }

    public abstract void resetRegisters();

    public abstract JsonArray toJsonArray();

}
