package dlsu.advcarc.opcode;

import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;

import java.util.ArrayList;
import java.util.List;

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

    public String toHexString(int desiredLength){
        return opcodeBinary.toHexString(desiredLength);
    }

    public String toHexString(){
        return toHexString(8);
    }

    public String toString(){
        return toHexString();
    }

    public List<String> getRegisterNamesToRead(){
        List<String> registers = new ArrayList<String>();


        return registers;
    }

    public boolean isBranchOrJump(){
        String instruction = getInstruction();
        return "BEQ".equals(instruction) || "J".equals(instruction);
    }

    public boolean isNOP(){
        return "NOP".equals(getInstruction());
    }

    public String getInstruction(){
        return  OpcodeHelper.getInstruction(opcodeBinary);
    }

    public String getARegisterName(){
        return ""; //TODO
    }

    public String getBRegisterName(){
        return ""; //TODO
    }

    public StringBinary getImm(){
        return new StringBinary(opcodeBinary.getBinaryValue().substring(16, 31));
    }

    public String getDestinationRegisterName(){
        return ""; //TODO
    }

    public static Opcode createNOP(){
        return new Opcode(StringBinary.valueOf(0).padBinaryValueStringBinary(32));
    }
}
