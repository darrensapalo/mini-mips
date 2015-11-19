package dlsu.advcarc.parser;

import dlsu.advcarc.utils.RadixHelper;

import java.math.BigInteger;

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

    public String padBinaryValue(int desiredLength){
        return RadixHelper.padWithZero(value, desiredLength);
    }

    public String getBinaryValue(){
        return value;
    }

    public long getAsLong(){
        return Long.valueOf(value, 2);
    }

    public double getAsDouble(){
        return Double.longBitsToDouble(new BigInteger(value, 2).longValue());
    }

    public String toHexString(){
        return toHexString(null);
    }

    public String toHexString(Integer desiredLength){
        String hex =  RadixHelper.convertBinaryToHexString(value);
        return desiredLength == null ? hex : RadixHelper.padWithZero(hex, desiredLength);
    }

    public String toString(){
        return RadixHelper.padWithZero(value, 64);
    }

    public StringBinary plus(StringBinary addend){
        return new StringBinary(Long.toBinaryString(Long.valueOf(value, 2) + Long.valueOf(addend.value, 2)));
    }

    public StringBinary minus(StringBinary minuend){
        return new StringBinary(Long.toBinaryString(Long.valueOf(value, 2) - Long.valueOf(minuend.value, 2)));
    }

    public StringBinary divide(StringBinary dividend){
        return new StringBinary(Long.toBinaryString(Long.valueOf(value, 2) / Long.valueOf(dividend.value, 2)));
    }

    public StringBinary times(StringBinary multiplicand){
        return new StringBinary(Long.toBinaryString(Long.valueOf(value, 2) * Long.valueOf(multiplicand.value, 2)));
    }




    public static StringBinary valueOf(long n){
        return new StringBinary(Long.toBinaryString(n));
    }

}
