package dlsu.advcarc.cpu.tracker;

import com.fasterxml.jackson.databind.util.JSONPObject;
import dlsu.advcarc.cpu.stage.Stage;
import dlsu.advcarc.parser.Code;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.ProgramCode;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;

/**
 * Created by admin on 11/21/2015.
 */
public class CPUCycleTracker {

    private int cycleNumber = 1;

//    private List<CycleRecord> cycleRecords;
//    private CycleRecord currCycleRecord;

    private HashMap<Integer, List<StageExecutionRecord>> map = new LinkedHashMap<Integer, List<StageExecutionRecord>>();
    private ProgramCode programCode;

    public CPUCycleTracker(ProgramCode programCode) {
        this.programCode = programCode;
        List<Code> codeList = programCode.getProgram();

        for (int i = 0; i < codeList.size(); i++) {
            Code code = codeList.get(i);
            map.put(i * 4, new ArrayList<StageExecutionRecord>());
        }
    }

    public void finishCycle(){
        for (Map.Entry<Integer, List<StageExecutionRecord>> entry : map.entrySet()) {
            if(entry.getValue().size() < cycleNumber )
                entry.getValue().add(new StageExecutionRecord(cycleNumber, ""));
        }
    }

    public void nextCycle() {
        cycleNumber++;
    }

    public void setIfInstruction(Instruction instruction) {

//        map.entrySet();
//        long memAddressLong = instruction.getMemAddressLong();
//        List<StageExecutionRecord> stageExecutionRecords = map.get(memAddressLong);
//        List<StageExecutionRecord> stageExecutionRecords2 = map.get(0);

        map.get(instruction.getMemAddressInt()).add(new StageExecutionRecord(cycleNumber, "IF"));




    }

    public void setIdInstruction(Instruction instruction) {
        map.get(instruction.getMemAddressInt()).add(new StageExecutionRecord(cycleNumber, "ID"));
    }

    public void setExInstruction(Instruction instruction) {
        map.get(instruction.getMemAddressInt()).add(new StageExecutionRecord(cycleNumber, "EX"));
    }

    public void setMemInstruction(Instruction instruction) {
        map.get(instruction.getMemAddressInt()).add(new StageExecutionRecord(cycleNumber, "MEM"));
    }

    public void setWbInstruction(Instruction instruction) {
        map.get(instruction.getMemAddressInt()).add(new StageExecutionRecord(cycleNumber, "WB"));
    }

    public JsonObject toJsonObject() {
        return new JsonObject()
                .put("cycles", cycleNumber)
                .put("pipeline", getMapAsJsonArray());
    }

    private JsonArray getMapAsJsonArray() {
        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<Integer, List<StageExecutionRecord>> entry : map.entrySet()) {
            jsonArray.add(new JsonObject()
                    .put("instruction", programCode.getCode(entry.getKey()))
                    .put("records", getListStageExecutionRecordsAsJsonArray(entry.getValue()))
            );
        }
        return jsonArray;
    }

    private JsonArray getListStageExecutionRecordsAsJsonArray(List<StageExecutionRecord> records){
        JsonArray jsonArray = new JsonArray();

        for(StageExecutionRecord record : records){
            jsonArray.add(record.getStage());
        }
        return jsonArray;
    }

}
