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

    public StringBinary clone(){
        return new StringBinary(value);
    }

    public String padBinaryValue(int desiredLength){
        return RadixHelper.padWithZero(value, desiredLength);
    }

    public StringBinary padBinaryValueStringBinary(int desiredLength){
        return new StringBinary(padBinaryValue(desiredLength));
    }

    public String padBinaryValueArithmetic(int desiredLength){ return RadixHelper.padArithmetic(value, desiredLength);}

    public StringBinary padBinaryValueArithmeticStringBinary(int desiredLength){
        return new StringBinary(padBinaryValueArithmetic(desiredLength));
    }

    public String forceLength(int desiredLength){
        return RadixHelper.forceLength(value, desiredLength);
    }

    public StringBinary forceLengthStringBinary(int desiredLength){
        return new StringBinary(forceLength(desiredLength));
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


    public int getAsInt() {
        return Integer.parseUnsignedInt(value, 2);
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
        return new StringBinary(Long.toBinaryString(new BigInteger(value, 2).add(new BigInteger(addend.value, 2)).longValue()));
    }

    public StringBinary minus(StringBinary minuend){
        return new StringBinary(Long.toBinaryString(new BigInteger(value, 2).subtract(new BigInteger(minuend.value, 2)).longValue()));
    }

    public StringBinary divide(StringBinary dividend){
        return new StringBinary(Long.toBinaryString(new BigInteger(value, 2).divide(new BigInteger(dividend.value, 2)).longValue()));
    }

    public StringBinary times(StringBinary multiplicand){
        return new StringBinary(Long.toBinaryString(new BigInteger(value, 2).multiply(new BigInteger(multiplicand.value, 2)).longValue()));
    }




    public static StringBinary valueOf(long n){
        return new StringBinary(Long.toBinaryString(n));
    }

    public static StringBinary valueOf(double d){
        return new StringBinary(Long.toBinaryString(Double.doubleToLongBits(d)));
    }


    public StringBinary and(StringBinary b) {
        String thisBinary = padBinaryValue(64);
        String thatBinary = b.padBinaryValue(64);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 64; i++)
        {
            if (thisBinary.charAt(i) == '1' && thatBinary.charAt(i) == '1')
                builder.append("1");
            else
                builder.append("0");
        }
        return new StringBinary(builder.toString());
    }

    /**
     * Shifting by a negative amount is effectively a shift-left
     * @param amount the number of bits to shift
     * @return the shifted binary
     */
    public StringBinary shiftRight(int amount) {
        String thisBinary = padBinaryValue(64);
        if (amount > 0) {
            for (int i = 0; i < amount; i++)
                thisBinary = "0" + thisBinary.substring(0, 63);
        }else if (amount < 0){
            for (int i = amount; i < 0; i++)
                thisBinary = thisBinary.substring(1, 64) + "0";
        }
        return new StringBinary(thisBinary);
    }

    public StringBinary or(StringBinary b) {
        String thisBinary = padBinaryValue(64);
        String thatBinary = b.padBinaryValue(64);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 64; i++)
        {
            if (thisBinary.charAt(i) == '1' || thatBinary.charAt(i) == '1')
                builder.append("1");
            else
                builder.append("0");
        }
        return new StringBinary(builder.toString());
    }

}
