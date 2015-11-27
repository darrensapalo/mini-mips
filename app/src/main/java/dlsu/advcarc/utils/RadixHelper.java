package dlsu.advcarc.utils;

import dlsu.advcarc.parser.StringBinary;

/**
 * Created by user on 11/19/2015.
 */
public class RadixHelper {

    public static StringBinary convertLongToStringBinary(long n) {
        return new StringBinary(Long.toBinaryString(n));
    }

    public static String convertBinaryToHexString(String value) {

        /* Pad with 0s to make the string length a multiple of 4 */
        int zerosToPad = (4 - value.length() % 4) % 4;
        value = RadixHelper.padWithZero(value, value.length() + zerosToPad);

        /* Convert per hex character */
        String hexString = "";
        for (int i = value.length() - 1; i > 0; i -= 4) {
            String currSetOfFour = value.substring(i - 3, i + 1);
            String currHexChar = Long.toHexString(Long.valueOf(currSetOfFour, 2));
            hexString = currHexChar + hexString;
        }

        /* Make it uppercase */
        return hexString.toUpperCase();
    }

    public static StringBinary convertHexToStringBinary(String value) {
        return convertLongToStringBinary(Long.valueOf(value, 16));
    }

    public static String convertLongToHexString(long n) {
        return padWithZero(Long.toHexString(n), 4).toUpperCase();
    }

    public static String padWithZero(String string, int desiredLength) {
        int zeroesToAdd = desiredLength - string.length();
        for (int i = 0; i < zeroesToAdd; i++)
            string = "0" + string;
        return string;
    }

    public static String forceLength(String string, int desiredLength) {
        if (string.length() < desiredLength)
            return padWithZero(string, desiredLength);

        int excess = string.length() - desiredLength;
        return string.substring(excess);
    }
}
