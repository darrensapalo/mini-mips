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

        if(isNOP())
            return registers;

        switch (getInstructionType()){

            case "J":
                break;

            case "R":
            case "Rx":
                registers.add(getARegisterName());
                registers.add(getBRegisterName());
                break;

            case "I":
                registers.add(getBRegisterName());
                break;
        }

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

    public String getInstructionType(){return OpcodeHelper.getInstructionType(opcodeBinary);}

    public String getARegisterName(){

        switch (getInstructionType()){

            case "J":
            case "R":
                int registerNumber = OpcodeHelper.getInt(opcodeBinary, 6, 10);
                return "R"+registerNumber;

            case "Rx":
                registerNumber = OpcodeHelper.getInt(opcodeBinary, 11, 15);
                return "F"+registerNumber;

            case "I":
                if("DSLL".equals(getInstruction()))
                    registerNumber = OpcodeHelper.getInt(opcodeBinary, 11, 15);
                else
                    registerNumber = OpcodeHelper.getInt(opcodeBinary, 6, 10);
                return "R"+registerNumber;
        }

        return "";
    }

    public String getBRegisterName(){

        switch (getInstructionType()){

            case "J":
            case "R":
                int registerNumber = OpcodeHelper.getInt(opcodeBinary, 11, 15);
                return "R"+registerNumber;

            case "Rx":
                registerNumber = OpcodeHelper.getInt(opcodeBinary, 16, 20);
                return "F"+registerNumber;

            case "I":
                String instruction = getInstruction();

                if("DSLL".equals(instruction))
                    registerNumber = OpcodeHelper.getInt(opcodeBinary, 16, 20);
                else
                    registerNumber = OpcodeHelper.getInt(opcodeBinary, 11, 15);

                String registerType = "L.S".equals(instruction) || "S.S".equals(instruction) ? "F" : "R";
                return registerType+registerNumber;
        }

        return "";
    }

    public StringBinary getImm(){
        return getSubBinary(16, 31);
    }

    public StringBinary getSubBinary(int startIndex, int endIndex){
        return new StringBinary(opcodeBinary.getBinaryValue().substring(startIndex, endIndex+1));
    }

    public String getDestinationRegisterName(){

        if(isNOP())
            return null;


        switch (getInstructionType()){

            case "J":
                return null;

            case "R":

                if(getInstruction().equals("DMULT"))
                    return null;

                int registerNumber = OpcodeHelper.getInt(opcodeBinary, 16, 20);
                return "R" + registerNumber;

            case "Rx":
                registerNumber = OpcodeHelper.getInt(opcodeBinary, 21, 25);
                return "F"+registerNumber;

            case "I":

                String instruction = getInstruction();

                switch (instruction){
                    case "BEQ":
                    case "SW":
                    case "S.S":
                            return null;

                    case "DSLL":
                        registerNumber = OpcodeHelper.getInt(opcodeBinary, 16, 20);
                        return "R"+registerNumber;

                    case "L.S":
                        registerNumber = OpcodeHelper.getInt(opcodeBinary, 11, 15);
                        return "F"+registerNumber;

                    default: //ANDI, DADDIU, LW, LWU
                        registerNumber = OpcodeHelper.getInt(opcodeBinary, 11, 15);
                        return "R"+registerNumber;
                }
        }

        return null;
    }

    public static Opcode createNOP(){
        return new Opcode(StringBinary.valueOf(0).padBinaryValueStringBinary(32));
    }
}
