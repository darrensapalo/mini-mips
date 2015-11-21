package dlsu.advcarc.cpu.tracker;

import io.vertx.core.json.JsonArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 11/21/2015.
 */
public class CPUCycleTracker {

    private int cycleNumber = 1;

    private List<CycleRecord> cycleRecords;
    private CycleRecord currCycleRecord;


    public CPUCycleTracker(){
        cycleRecords = new ArrayList<CycleRecord>();
        currCycleRecord = new CycleRecord(cycleNumber);
    }

    public void nextCycle(){
        if(currCycleRecord != null)
            cycleRecords.add(currCycleRecord);
        currCycleRecord = new CycleRecord(cycleNumber++);
    }

    public void setIfInstruction(String ifInstruction) {
        currCycleRecord.setIfInstruction(ifInstruction);
    }

    public void setIdInstruction(String idInstruction) {
        currCycleRecord.setIdInstruction(idInstruction);
    }

    public void setExInstruction(String exInstruction) {
        currCycleRecord.setExInstruction(exInstruction);
    }

    public void setMemInstruction(String memInstruction) {
        currCycleRecord.setMemInstruction(memInstruction);
    }

    public void setWbInstruction(String wbInstruction) {
        currCycleRecord.setIfInstruction(wbInstruction);
    }

    public JsonArray toJsonArray(){
        JsonArray jsonArray = new JsonArray();
        for(CycleRecord cycleRecord: cycleRecords){
            jsonArray.add(cycleRecord.toJsonObject());
        }
        return jsonArray;
    }




}
