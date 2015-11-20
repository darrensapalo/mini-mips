package dlsu.advcarc.memory;

import dlsu.advcarc.cpu.ExecutionManager;
import dlsu.advcarc.parser.Code;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.RegisterManager;
import dlsu.advcarc.server.Addresses;
import dlsu.advcarc.server.EventBusHolder;
import dlsu.advcarc.utils.RadixHelper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by user on 11/19/2015.
 */
public class MemoryManager {

    /* Singleton */
    private static MemoryManager instance;
    public static MemoryManager instance() {
        if(instance == null)
            instance = new MemoryManager();
        return instance;
    }

    /* Class */

    public static final int DATA_SEGMENT_SIZE = 8192;

    private List<Memory> ram = new ArrayList<Memory>();

    private MemoryManager(){
        initRam();
    }

    private void initRam(){
        ram.clear();
        for(int i=0;i<2048;i++){
            long memLocation = DATA_SEGMENT_SIZE + 4 * i;
            ram.add(new Memory(RadixHelper.convertLongToHexString(memLocation)));
        }
    }

    public Memory getInstance(String memoryLocation) throws IllegalArgumentException {
        if(!Memory.validate(memoryLocation))
            throw new IllegalArgumentException("Invalid memory format: "+memoryLocation);

        int memoryIndex = (Integer.valueOf(memoryLocation, 16) - DATA_SEGMENT_SIZE) / 4;
        return ram.get(memoryIndex);
    }

    public void updateMemory(String memoryLocation, StringBinary newValue){
        Memory memory = getInstance(memoryLocation);
        memory.setValue(newValue);

          /* Broadcast the Updated Memory Values */
        EventBusHolder.instance()
                .getEventBus()
                .publish(Addresses.MEMORY_BROADCAST,
                        MemoryManager.instance().getDataJsonArray()
                );

    }

    public void clear() {
        ram.clear();
    }

    public JsonArray getCodeJsonArray(){
        JsonArray codeMemoryArray = ExecutionManager.instance().getProgramCode().toJsonArray(false);

        int numLinesOfCode = codeMemoryArray.size();
        int numLinesMissing = DATA_SEGMENT_SIZE / 4 - numLinesOfCode;
        int startAddingCodeAt = numLinesOfCode * 4;

        for(int i=0;i<numLinesMissing;i++){
            codeMemoryArray.add(Code.createNOP(startAddingCodeAt + i*4).toJsonObject(false));
        }
        return codeMemoryArray;
    }

    public JsonArray getDataJsonArray(){
        JsonArray jsonArray = new JsonArray();
        for(Memory memory: ram)
            jsonArray.add(memory.toJsonObject());
        return jsonArray;
    }

}
