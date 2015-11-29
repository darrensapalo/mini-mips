package dlsu.advcarc.cpurevised;

import dlsu.advcarc.cpurevised.CPUCycleTracker;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.server.Addresses;
import dlsu.advcarc.server.EventBusHolder;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 11/29/2015.
 */
public class CPU {

    private IFStage ifStage;
    private IDStage idStage;
    private EXSwitch exStage;
    private MEMStage memStage;
    private WBStage wbStage;

    private ProgramCode programCode;
    private boolean isFlushing;
    private boolean justFinishedBranchEx;

    private CPUCycleTracker cpuCycleTracker;

    /* Initialization Code */

    public CPU(){
        reset();
    }

    private void reset(){
        ifStage = new IFStage(this);
        idStage = new IDStage(this);
        exStage = new EXSwitch();
        memStage = new MEMStage(this);
        wbStage = new WBStage();
        programCode = null;
        isFlushing  = false;
        justFinishedBranchEx = false;
    }

    public void inputProgramCode(ProgramCode programCode){
        reset();
        this.programCode = programCode;
        cpuCycleTracker = new CPUCycleTracker(programCode);
    }


    /* Exeuction-Related Methods */

    public boolean clock(){

        // Housekeeping
        wbStage.resetToNOP();

        if(memStage.hasInstructionToForward() && wbStage.isReadyToAcceptInstruction()) {
            wbStage.housekeeping(memStage);
            memStage.resetToNOP();
        }
        if(exStage.hasInstructionToForward() && memStage.isReadyToAcceptInstruction()) {
            memStage.housekeeping(exStage);
            exStage.resetToNOP();
        }
        if(idStage.hasInstructionToForward() && exStage.isReadyToAcceptInstruction()) {
            exStage.housekeeping(idStage);
            idStage.resetToNOP();
        }
        if(ifStage.hasInstructionToForward() && idStage.isReadyToAcceptInstruction()) {
            idStage.housekeeping(ifStage);
            ifStage.resetToNOP();
        }

        // Execute
        boolean wbExecuted = wbStage.execute();
        boolean memExecuted =  memStage.execute();
        boolean exExecuted = exStage.execute();
        boolean idExecuted = idStage.execute();
        boolean ifExecuted =  ifStage.execute();

        // Record the executions
        if(wbExecuted)
            cpuCycleTracker.setWbInstruction(wbStage.getIRMemAddressHex());
        if(memExecuted)
            cpuCycleTracker.setMemInstruction(memStage.getIRMemAddressHex());
        if(exExecuted)
            cpuCycleTracker.setExInstruction(exStage.getIR().getInstruction(), exStage.getIRMemAddressHex());
        if(idExecuted)
            cpuCycleTracker.setIdInstruction(idStage.getIRMemAddressHex());
        if(ifExecuted)
            cpuCycleTracker.setIfInstruction(ifStage.getIRMemAddressHex());

        broadcastCPUState();

        return true; //TODO
    }

    public boolean hasPendingWrite(String registerName){
        List<AbstractStage> stagesToCheck = new ArrayList<AbstractStage>();
        stagesToCheck.add(exStage);
        stagesToCheck.add(memStage);
        stagesToCheck.add(wbStage);

        for(AbstractStage stage: stagesToCheck){
            if(registerName.equals(stage.getIR().getDestinationRegisterName()))
                return true;
        }

        return false;
    }

    public void broadcastCPUState() {
        EventBusHolder.instance().getEventBus()
                .publish(Addresses.CPU_BROADCAST, this.toJsonObject());
    }

    /* Getters and Setters */

    public EXIntegerStage getEXIntegerStage (){
        return exStage.getEXIntegerStage();
    }

    public boolean isFlushing() {
        return isFlushing;
    }

    public void setFlushing(boolean flushing) {
        isFlushing = flushing;
    }

    public boolean justFinishedBranchEx() {
        return justFinishedBranchEx;
    }

    public void setJustFinishedBranchEx(boolean justFinishedBranchEx) {
        this.justFinishedBranchEx = justFinishedBranchEx;
    }

    /* Json Methods */

    public JsonObject toJsonObject() {
        return new JsonObject()
                .put("registers", getRegistersJsonArray())
                .put("pipeline", ""); //TODO cycleTracker == null ? new JsonArray() : cycleTracker.toJsonObject());
    }

    public JsonArray getRegistersJsonArray() {

        return new JsonArray()
                .addAll(ifStage.toJsonArray())
                .addAll(idStage.toJsonArray())
                .addAll(exStage.toJsonArray())
                .addAll(memStage.toJsonArray())
                ;
    }
}
