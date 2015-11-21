package dlsu.advcarc.cpu.tracker;

import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 11/21/2015.
 */
public class CycleRecord {

    private int cycleNumber;

    private String ifInstruction;
    private String idInstruction;
    private String exInstruction;
    private String memInstruction;
    private String wbInstruction;


    public CycleRecord(int cycleNumber){
        this.cycleNumber = cycleNumber;
        ifInstruction = "";
        idInstruction = "";
        exInstruction = "";
        memInstruction = "";
        wbInstruction = "";
    }

    public void setIfInstruction(String ifInstruction) {
        this.ifInstruction = ifInstruction;
    }

    public void setIdInstruction(String idInstruction) {
        this.idInstruction = idInstruction;
    }

    public void setExInstruction(String exInstruction) {
        this.exInstruction = exInstruction;
    }

    public void setMemInstruction(String memInstruction) {
        this.memInstruction = memInstruction;
    }

    public void setWbInstruction(String wbInstruction) {
        this.wbInstruction = wbInstruction;
    }

    public JsonObject toJsonObject(){
        return new JsonObject()
                .put("cycle", cycleNumber)
                .put("if", ifInstruction)
                .put("id", idInstruction)
                .put("ex", exInstruction)
                .put("mem", memInstruction)
                .put("wb", wbInstruction);

    }
}
