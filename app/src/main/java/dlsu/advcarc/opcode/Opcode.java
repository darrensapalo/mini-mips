package dlsu.advcarc.opcode;

import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;

/**
 * Created by user on 11/20/2015.
 */
public class Opcode {

    private StringBinary opcodeString;

    public Opcode(StringBinary opcodeString){
        this.opcodeString = opcodeString;
    }

    public String toHexString(){
        return opcodeString.toHexString();
    }

    public String toString(){
        return opcodeString.toHexString(8);
    }
}
