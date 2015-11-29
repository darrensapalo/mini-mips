package dlsu.advcarc.cpurevised;

import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;

/**
 * Created by user on 11/29/2015.
 */
public class EXSwitch extends AbstractStage {

    private EXIntegerStage integer;

    public EXSwitch(){
        integer = new EXIntegerStage();
    }

    @Override
    public boolean hasInstructionToForward() {
        return integer.hasInstructionToForward();
    }

    @Override
    public boolean  execute() {
        return integer.execute();
        //TODO add the adder and multiplier

        //TODO add dependency checking for Write after Write (only applies to S.S after ADD.S or MULT.S)
    }

    @Override
    public JsonArray toJsonArray() {
        return integer.toJsonArray();
    }

    @Override
    public void housekeeping(AbstractStage previousStage) {
        //TODO select appropriate EX component
        integer.housekeeping(previousStage);
    }

    @Override
    public void resetRegisters() {
        //TODO check if we need to do anything here
        if(integer != null)
            integer.resetToNOP();
    }

    public  boolean isReadyToAcceptInstruction(String instruction){

        //TODO check for ADD.S and MUL.S
        return integer.isReadyToAcceptInstruction();
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

    public String getIRMemAddressHex(){
        return integer.getIRMemAddressHex();
    }

    public EXIntegerStage getEXIntegerStage(){
        return integer;
    }
}
