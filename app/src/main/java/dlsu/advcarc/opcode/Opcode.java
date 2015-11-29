package dlsu.advcarc.opcode;

import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;

/**
 * Created by user on 11/20/2015.
 */
public class Opcode {

    private StringBinary opcodeBinary;

    public Opcode(StringBinary opcodeBinary){
        this.opcodeBinary = opcodeBinary.forceLengthStringBinary(32);
    }

    public String to32BitString(){
        return opcodeBinary.forceLength(32);
    }

    public String toHexString(){
        return opcodeBinary.toHexString(8);
    }

    public String toString(){
        return toHexString();
    }
}
