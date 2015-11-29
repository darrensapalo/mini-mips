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
    protected CPU cpu;
    protected boolean isStalling;

    protected AbstractStage(CPU cpu){
        this.cpu = cpu;
        resetToNOP();
    }

    protected boolean canExecute(AbstractStage nextStage){

        // If next stage is stalling, this also has to stall
        if(nextStage != null && nextStage.isStalling())
            return false;

        // If flushing, and instruction is after the running branch
        if(cpu.isBranchRunning() && cpu.isInstructionAfterRunningBranch(IRMemAddressHex) && !(this instanceof IFStage))
            return false;

        if(!checkExtraDependenciesIfCanExecute())
            return false;

        return true;
    }

    public abstract boolean hasInstructionToForward();

    public boolean executeIfAllowed(AbstractStage nextStage){

        // No need to do anything if NOP, unless IF
        if(!(this instanceof IFStage) && isNOP()) {
//            isStalling = true;
            return false;
        }

        // Check control hazards and stalling
        if(canExecute(nextStage)) {
            isStalling = false;
            execute();
            return true;
        }
        else {
            isStalling = true;
            return false;
        }
    }

    protected abstract boolean checkExtraDependenciesIfCanExecute();

    protected abstract void execute();

    public abstract void housekeeping(AbstractStage previousStage);

    public void resetToNOP(){
        IR = Opcode.createNOP();
        IRMemAddressHex = StringBinary.valueOf(0).toHexString();
        isStalling = false;
        resetRegisters();
    }

    public abstract void resetRegisters();

    public abstract JsonArray toJsonArray();

    public String getIRString(){
        if(IR == null)
            IR = Opcode.createNOP();
        return IR.toHexString(16);
    }

    public Opcode getIR() {
        return IR;
    }

    public boolean isNOP(){
        return IR.isNOP();
    }

    public boolean isStalling() {
        return isStalling;
    }

    public String getIRMemAddressHex() {
        return IRMemAddressHex;
    }
}
