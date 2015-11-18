package dlsu.advcarc.memory;

import dlsu.advcarc.utils.RadixHelper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 11/19/2015.
 */
public class MemoryManager {

    private static MemoryManager instance;

    public static MemoryManager instance() {
        if(instance == null)
            instance = new MemoryManager();
        return instance;
    }

    private List<Memory> ram = new ArrayList<Memory>();

    private MemoryManager(){
        initRam();
    }

    private void initRam(){
        ram.clear();
        for(int i=0;i<2048;i++){
            long memLocation = 8192 + 4 * i;
            ram.add(new Memory(RadixHelper.convertLongToHexString(memLocation)));
        }
    }

    public Memory getInstance(String memory) throws IllegalArgumentException {
        if(!Memory.validate(memory))
            throw new IllegalArgumentException("Invalid memory format: "+memory);

        int memoryIndex = (Integer.valueOf(memory, 16) - 8192) / 4;
        return ram.get(memoryIndex);
    }

    public void clear() {
        ram.clear();
    }

    public JsonObject toJsonObject(){

        JsonObject jsonObject = new JsonObject();

        // get code memory
        // TODO get this from ProgramCode

        // get data memory
        JsonArray dataMemoryArray = getDataJsonArray();

        jsonObject.put("data", dataMemoryArray);
        return jsonObject;
    }

    private JsonArray getDataJsonArray(){
        JsonArray jsonArray = new JsonArray();
        for(Memory memory: ram)
            jsonArray.add(memory.toJsonObject());
        return jsonArray;
    }

}
