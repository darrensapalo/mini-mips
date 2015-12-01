package dlsu.advcarc.cpurevised;

import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 12/1/2015.
 */
public class EXInstruction {

    public StringBinary A;
    public StringBinary B;
    public StringBinary IMM;
    public Opcode IR;
    public String IRMemAddressHex;
    public StringBinary NPC;

    public JsonArray toJsonArray(int number, String type){
        return new JsonArray()
                .add(new JsonObject().put("register", type+number+" IR").put("value",IR.toHexString()));
    }

}
