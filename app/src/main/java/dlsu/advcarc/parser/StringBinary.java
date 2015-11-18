package dlsu.advcarc.parser;

/**
 * Created by Darren on 11/16/2015.
 */
public class StringBinary {

    private String value;

    public StringBinary(String value) throws NumberFormatException{
        if (value.length() > 64)
            throw new NumberFormatException("Invalid amount; Cannot write more than 64 bits.");

        String others = value.replaceAll("[^01]+", "");
        if (others.length() != value.length())
            throw new NumberFormatException("Invalid format; Should only use 1s or 0s.");

        this.value = value;
    }

    public String getBinaryValue(){
        return value;
    }

    public String toHexString(){
        return Integer.toHexString(Integer.valueOf(value, 2));
    }
}
