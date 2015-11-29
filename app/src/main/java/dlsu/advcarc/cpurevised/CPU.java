package dlsu.advcarc.cpurevised;

import dlsu.advcarc.parser.ProgramCode;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

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
    }


    /* Exeuction-Related Methods */

    public boolean clock(){

        // Execute
        wbStage.execute();
        memStage.execute();
        exStage.execute();
        idStage.execute();
        ifStage.execute();

        // Housekeeping
        if(memStage.hasInstructionToForward())
            wbStage.housekeeping(memStage);
        if(exStage.hasInstructionToForward())
            memStage.housekeeping(exStage);
        if(idStage.hasInstructionToForward())
            exStage.housekeeping(idStage);
        if(ifStage.hasInstructionToForward())
            idStage.housekeeping(ifStage);

        return true; //TODO
    }

    public boolean hasPendingWrite(String registerName){
        return false; //TODO
    }


    /* Getters and Setters */

    public EXIntegerStage getEXIntegerStage (){
        return null; //TODO
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
//                .addAll(idStage.toJsonArray())
//                .addAll(exStage.toJsonArray())
//                .addAll(memStage.toJsonArray())
                ;
    }
}
