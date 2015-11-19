package dlsu.advcarc.parser;

import dlsu.advcarc.utils.RadixHelper;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/19/2015.
 */
public class Code {

    public static final String NOP = "nop";

    private String label;
    private String line;
    private int memoryLocation;

    public Code(String line, int memoryLocation) {
        this.label = InstructionChecker.parseLabel(line);
        this.line = ((label == null) ? line : line.replace(label+":", "").trim());
        this.memoryLocation = memoryLocation;
    }

    public String getLine() {
        return line;
    }

    public int getMemoryLocation() {
        return memoryLocation;
    }

    public String getMemoryLocationHex() {
        return RadixHelper.padWithZero(Integer.toHexString(memoryLocation).toUpperCase(),4);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return getMemoryLocationHex() + ": " + line;
    }

    public String getOpcode(){
        if(NOP.equals(line))
            return "00000000";
        return "00000000"; //TODO generate correct opcode
    }

    public JsonObject toJsonObject(boolean includeInstruction){
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("mem", getMemoryLocationHex());
        jsonObject.put("opcode", getOpcode());
        if(includeInstruction)
            jsonObject.put("instruction", (label != null) ? label + ": "+line :  line);

        return jsonObject;
    }

    public static Code createNOP(int memoryLocation){
        return new Code(NOP, memoryLocation);
    }
}