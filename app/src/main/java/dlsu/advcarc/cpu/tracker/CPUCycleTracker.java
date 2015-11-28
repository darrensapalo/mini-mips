package dlsu.advcarc.cpu.tracker;

import dlsu.advcarc.parser.Code;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.ProgramCode;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;

/**
 * Created by admin on 11/21/2015.
 */
public class CPUCycleTracker {

    private int cyclesFinished = 0;
    private HashMap<Integer, List<String>> map = new LinkedHashMap<Integer, List<String>>();
    private ProgramCode programCode;

    public CPUCycleTracker(ProgramCode programCode) {
        this.programCode = programCode;
        List<Code> codeList = programCode.getProgram();

        for (int i = 0; i < codeList.size(); i++) {
            Code code = codeList.get(i);
            map.put(i * 4, new ArrayList<String>());
        }
    }

    public void nextCycle() {

        if(map.size() == 0)
            return;

        for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
            if(entry.getValue().size() < cyclesFinished + 1)
                entry.getValue().add("");
        }
        cyclesFinished++;
    }

    public int getCycleNumber() {
        return cyclesFinished;
    }

    public void setIfInstruction(Instruction instruction) {
        map.get(instruction.getMemAddressInt()).add("IF");
    }

    public void setIdInstruction(Instruction instruction) {
        map.get(instruction.getMemAddressInt()).add("ID");
    }

    public void setExInstruction(Instruction instruction) {

        String instructionString = instruction.getInstruction();
        List<String> target = map.get(instruction.getMemAddressInt());

        if(instructionString.contains("ADD.S")) {

            int digit = 1;

            if(target.size() > 0 && target.get(target.size()-1).matches("A\\d"))
                digit = Integer.parseInt(target.get(target.size()-1).replaceAll("A",""))+1;

            target.add("A"+digit);
        }
        else if(instructionString.contains("MUL.S")){

            int digit = 1;

            if(target.size() > 0 && target.get(target.size()-1).matches("M\\d"))
                digit = Integer.parseInt(target.get(target.size()-1).replaceAll("M",""))+1;

            target.add("M"+digit);
        }
        else
            target.add("EX");
    }

    public void setMemInstruction(Instruction instruction) {
        map.get(instruction.getMemAddressInt()).add("MEM");
    }

    public void setWbInstruction(Instruction instruction) {
        map.get(instruction.getMemAddressInt()).add("WB");
    }

    public JsonObject toJsonObject() {
        return new JsonObject()
                .put("cycles", cyclesFinished)
                .put("pipeline", getMapAsJsonArray());
    }

    private JsonArray getMapAsJsonArray() {
        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
            jsonArray.add(new JsonObject()
                    .put("instruction", programCode.getCode(entry.getKey()))
                    .put("records", getStringListAsJsonArray(entry.getValue()))
            );
        }
        return jsonArray;
    }

    private JsonArray getStringListAsJsonArray(List<String> records){
        JsonArray jsonArray = new JsonArray();

        for(String record : records){
            jsonArray.add(record);
        }
        return jsonArray;
    }

}
