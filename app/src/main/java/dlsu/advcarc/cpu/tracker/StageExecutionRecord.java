package dlsu.advcarc.cpu.tracker;

import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/24/2015.
 */
public class StageExecutionRecord {

    private int cycleNumber;
    private String stage;

    public StageExecutionRecord(int cycleNumber, String stage){
        this.cycleNumber = cycleNumber;
        this.stage = stage;
    }

    public JsonObject toJsonObject(){
        return new JsonObject()
                .put("cycle", cycleNumber)
                .put("stage", stage);
    }

    public String getStage() {
        return stage;
    }
}
