package dlsu.advcarc.cpurevised;

import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.Register;
import dlsu.advcarc.server.Addresses;
import dlsu.advcarc.server.EventBusHolder;
import dlsu.advcarc.utils.RadixHelper;
import io.vertx.core.json.Json;
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
    private boolean ifStageCanCheckCond;
    private String runningBranchMemAddressHex;

    private CPUCycleTracker cpuCycleTracker;

    private StringBinary HI;
    private StringBinary LO;

    /* Initialization Code */

    public CPU(){
        reset();
    }

    private void reset(){
        ifStage = new IFStage(this);
        idStage = new IDStage(this);
        exStage = new EXSwitch(this);
        memStage = new MEMStage(this);
        wbStage = new WBStage(this);
        programCode = null;
        ifStageCanCheckCond = false;
        runningBranchMemAddressHex = null;
        HI = StringBinary.valueOf(0).padBinaryValueStringBinary(64);
        LO = StringBinary.valueOf(0).padBinaryValueStringBinary(64);
    }

    public void inputProgramCode(ProgramCode programCode){
        reset();
        this.programCode = programCode;
        cpuCycleTracker = new CPUCycleTracker(programCode);
        broadcastCPUState();
    }


    /* Exeuction-Related Methods */

    public boolean clock(){

        // Housekeeping
        wbStage.resetToNOP();

        if(!memStage.isStalling() && !memStage.isNOP() && wbStage.isNOP()) {
            wbStage.housekeeping(memStage);
            memStage.resetToNOP();
        }
        AbstractEXStage exStageToForward = exStage.getStageToForward();
        if(exStageToForward != null &&  memStage.isNOP()) {
            memStage.housekeeping(exStageToForward);
            exStage.resetToNOP();
        }
        if(!idStage.isStalling() && !idStage.isNOP() &&  exStage.isTargetStageNOP(idStage.getIR())) {
            exStage.housekeeping(idStage);
            idStage.resetToNOP();
        }
        if(!ifStage.isStalling() && !ifStage.isNOP() &&  idStage.isNOP() && !exStage.hasStall()) {
            idStage.housekeeping(ifStage);
            ifStage.resetToNOP();
        }


        // Execute
        boolean wbExecuted = wbStage.executeIfAllowed(null);
        boolean memExecuted =  memStage.executeIfAllowed(wbStage);
        boolean exExecuted = exStage.executeIfAllowed(memStage);
        boolean idExecuted = idStage.executeIfAllowed(exStage.getTargetStage(idStage.getIR()));
        boolean ifExecuted =  exStage.hasStall()? false : ifStage.executeIfAllowed(idStage);

        // Record the executions
        if(wbExecuted)
            cpuCycleTracker.setWbInstruction(wbStage.getIRMemAddressHex());
        if(memExecuted)
            cpuCycleTracker.setMemInstruction(memStage.getIRMemAddressHex());
        if(exExecuted) {
            List<Opcode> activeInstructions = exStage.getActiveInstructionsLastCycle();
            List<String> memAddresses = exStage.getActiveInstructionsMem();
            for(int i=0; i<activeInstructions.size(); i++)
                cpuCycleTracker.setExInstruction(activeInstructions.get(i).getInstruction(), memAddresses.get(i));
        }
        if(idExecuted)
            cpuCycleTracker.setIdInstruction(idStage.getIRMemAddressHex());
        if(ifExecuted)
            cpuCycleTracker.setIfInstruction(ifStage.getIRMemAddressHex());

        if(!hasFinishedExecuting())
            cpuCycleTracker.nextCycle();
        broadcastCPUState();

        if(ifStageCanCheckCond)
            runningBranchMemAddressHex = null;

        return !hasFinishedExecuting();
    }

    private boolean hasFinishedExecuting(){
        return ifStage.isNOP() && idStage.isNOP() && exStage.isNOP() && memStage.isNOP() && wbStage.isNOP();
    }

    public boolean hasPendingWrite(String registerName){
        List<AbstractStage> stagesToCheck = new ArrayList<AbstractStage>();
        stagesToCheck.add(exStage);
        stagesToCheck.add(memStage);
        stagesToCheck.add(wbStage);

        for(AbstractStage stage: stagesToCheck){
            if(stage.hasPendingWrite(registerName))
                return true;
        }

        return false;
    }

    public boolean checkWriteAfterWrite(String registerName){
        return exStage.hasPendingFloatingWrite(registerName);
    }


    public boolean isInstructionAfterRunningBranch(String instMemAddressHex){
        int instMemAddressInt = RadixHelper.convertHexToStringBinary(instMemAddressHex).getAsInt();
        int branchAddressInt = RadixHelper.convertHexToStringBinary(runningBranchMemAddressHex).getAsInt();

        return instMemAddressInt > branchAddressInt;
    }

    public void broadcastCPUState() {
        EventBusHolder.instance().getEventBus()
                .publish(Addresses.CPU_BROADCAST, this.toJsonObject());
    }


    /* Getters and Setters */

    public EXIntegerStage getEXIntegerStage (){
        return exStage.getEXIntegerStage();
    }

    public boolean isBranchRunning() {
        return runningBranchMemAddressHex != null && !runningBranchMemAddressHex.trim().isEmpty();
    }

    public void setRunningBranch(String runningBranchMemAddressHex) {
        this.runningBranchMemAddressHex = runningBranchMemAddressHex;
    }

    public boolean ifStageCanCheckCond() {
        return ifStageCanCheckCond;
    }

    public void setIfStageCanCheckCond(boolean ifStageCanCheckCond) {
        this.ifStageCanCheckCond = ifStageCanCheckCond;
    }

    public StringBinary getHI() {
        return HI;
    }

    public void setHI(StringBinary HI) {
        this.HI = HI;
    }

    public StringBinary getLO() {
        return LO;
    }

    public void setLO(StringBinary LO) {
        this.LO = LO;
    }

    /* Json Methods */

    public JsonObject toJsonObject() {
        return new JsonObject()
                .put("registers", getRegistersJsonArray())
                .put("pipeline", cpuCycleTracker == null? "" : cpuCycleTracker.toJsonObject()); //TODO cycleTracker == null ? new JsonArray() : cycleTracker.toJsonObject());
    }

    public JsonArray getRegistersJsonArray() {

        return new JsonArray()
                .addAll(ifStage.toJsonArray())
                .addAll(idStage.toJsonArray())
                .addAll(getHILOArray())
                .addAll(exStage.toJsonArray())
                .addAll(memStage.toJsonArray())
                .addAll(wbStage.toJsonArray())
                ;
    }

    private JsonArray getHILOArray(){
        return new JsonArray()
                .add(new JsonObject().put("register", "HI").put("value",  HI.toHexString(16)))
                .add(new JsonObject().put("register", "LO").put("value", LO.toHexString(16)));
    }

}
