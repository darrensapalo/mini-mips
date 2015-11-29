package dlsu.advcarc.cpurevised;

import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;

/**
 * Created by user on 11/29/2015.
 */
public class EXSwitch extends AbstractStage {

    private EXIntegerStage integer;

    public EXSwitch(CPU cpu){
        super(cpu);
        integer = new EXIntegerStage(cpu);
    }

    @Override
    public boolean hasInstructionToForward() {
        return integer.hasInstructionToForward();
    }

    @Override
    protected boolean checkExtraDependenciesIfCanExecute() {
        return integer.checkExtraDependenciesIfCanExecute();
    }

    @Override
    public boolean executeIfAllowed(AbstractStage nextStage){
        return integer.executeIfAllowed(nextStage);
        //TODO call adder and multiplier here || adder.executeIfAllowed() ...
    }

    @Override
    public void execute() {
        //This method should not be called
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

    @Override
    public boolean isNOP(){
        return integer.isNOP();
    }
    public AbstractStage getTargetStage(Opcode opcode){
        switch(opcode.getInstruction()){
            case "ADD.S":
                return null; //TODO replace with adder
            case "MUL.S":
                return null; // TODO Replace with multiplier
            default: return integer;
        }
    }

    public  boolean isReadyToAcceptInstruction(String instruction){

        //TODO check for ADD.S and MUL.S
        return integer.isNOP();
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
