package dlsu.advcarc.parser;

import dlsu.advcarc.utils.RadixHelper;

/**
 * Created by Darren on 11/16/2015.
 */
public class StringBinary {

    private String value;

    public StringBinary(String value) throws NumberFormatException{
        if (value.length() > 64)
            throw new NumberFormatException("Invalid amount; Cannot write more than 64 bits.");

        if(!value.matches("[01]+"))
            throw new NumberFormatException("Invalid format; Should only use 1s or 0s.");

        this.value = value;
    }

    public String getBinaryValue(){
        return value;
    }

    public String toHexString(){
        return RadixHelper.convertBinaryToHexString(value);
    }

    public String toString(){
        return RadixHelper.padWithZero(value, 64);
    }

}
