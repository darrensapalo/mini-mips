package dlsu.advcarc.parser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Darren on 11/21/2015.
 */
public class StringBinaryTest {
    @Test
    public void testShiftRight() throws Exception{
        StringBinary stringBinary = new StringBinary("0000000000000000000000000000000000000000000000000000000000000010");
        StringBinary result = stringBinary.shiftRight(2);
        assertEquals("0000000000000000000000000000000000000000000000000000000000000000", result.getBinaryValue());

        stringBinary = new StringBinary("0000000000000000000000000000000000000000000000000000000000000010");
        result = stringBinary.shiftRight(1);
        assertEquals("0000000000000000000000000000000000000000000000000000000000000001", result.getBinaryValue());

        stringBinary = new StringBinary("0000000000000000000000000000000000000000000000000000000000000010");
        result = stringBinary.shiftRight(-4);
        assertEquals("0000000000000000000000000000000000000000000000000000000000100000", result.getBinaryValue());

        stringBinary = new StringBinary("0000100000000000000000000000000000000000000000000000000000000010");
        result = stringBinary.shiftRight(-2);
        assertEquals("0010000000000000000000000000000000000000000000000000000000001000", result.getBinaryValue());
    }

    @Test
    public void testAnd() throws Exception {
        StringBinary stringBinary = new StringBinary("1101110111011101110111011101110111011101110111011101110111011101");
        StringBinary thatBinary = new StringBinary  ("1010101010101010101010101010101010101010101010101010101010101010");
        StringBinary result = stringBinary.and(thatBinary);
        assertEquals("1000100010001000100010001000100010001000100010001000100010001000", result.getBinaryValue());

        stringBinary = new StringBinary("0000000011111111000000001111111100000000111111110000000011111111");
        thatBinary = new StringBinary  ("1100000000111111110000000011111111000000001111111100000000111111");
        result = stringBinary.and(thatBinary);
        assertEquals("0000000000111111000000000011111100000000001111110000000000111111", result.getBinaryValue());
    }

    @Test
    public void testOr() throws Exception {
        StringBinary stringBinary = new StringBinary("0000000011111111000000001111111100000000111111110000000011111111");
        StringBinary thatBinary =   new StringBinary("1100000000111111110000000011111111000000001111111100000000111111");
        StringBinary result = stringBinary.or(thatBinary);
        assertEquals("1100000011111111110000001111111111000000111111111100000011111111", result.getBinaryValue());

        stringBinary = new StringBinary("1111111111111111111111111111111111111111111111111111111111111111");
        thatBinary = new StringBinary("1100000000111111110000000011111111000000001111111100000000111111");
        result = stringBinary.or(thatBinary);
        assertEquals(stringBinary.getBinaryValue(), result.getBinaryValue());

        stringBinary = new StringBinary("0000000000000000000000000000000000000000000000000000000000000000");
        thatBinary = new StringBinary("0000000000000000000000000000000000000000000000000000000000001100");
        result = stringBinary.or(thatBinary);
        assertEquals("0000000000000000000000000000000000000000000000000000000000001100", result.getBinaryValue());
    }


    @Test
    public void testPlus() throws Exception {
        StringBinary stringBinary = new StringBinary("0000000000000000000000000000000000000000000000000000000000001000");
        StringBinary thatBinary =   new StringBinary("0000000000000000000000000000000000000000000000000000000000000100");
        StringBinary result = stringBinary.plus(thatBinary);
        assertEquals(12, result.getAsInt());

        stringBinary = new StringBinary("0000000000000000000000000000000000000000000000000000000000001000");
        thatBinary =   new StringBinary("0000000000000000000000000000000000000000000000000000000000001000");
        result = stringBinary.plus(thatBinary);
        assertEquals(16, result.getAsInt());
    }

    @Test
    public void testMinus() throws Exception {
        StringBinary stringBinary = new StringBinary("0000000000000000000000000000000000000000000000000000000000001000");
        StringBinary thatBinary =   new StringBinary("0000000000000000000000000000000000000000000000000000000000000100");
        StringBinary result = stringBinary.minus(thatBinary);
        assertEquals(4, result.getAsInt());

        stringBinary = new StringBinary("0000000000000000000000000000000000000000000000000000000000001000");
        thatBinary =   new StringBinary("0000000000000000000000000000000000000000000000000000000000001000");
        result = stringBinary.minus(thatBinary);
        assertEquals(0, result.getAsInt());
    }

    @Test
    public void testDivide() throws Exception {
        StringBinary stringBinary = new StringBinary("0000000000000000000000000000000000000000000000000000000000001000");
        StringBinary thatBinary =   new StringBinary("0000000000000000000000000000000000000000000000000000000000000100");
        StringBinary result = stringBinary.plus(thatBinary);
        assertEquals(12, result.getAsInt());

        stringBinary = new StringBinary("0000000000000000000000000000000000000000000000000000000000001000");
        thatBinary =   new StringBinary("0000000000000000000000000000000000000000000000000000000000001000");
        result = stringBinary.plus(thatBinary);
        assertEquals(16, result.getAsInt());
    }

    @Test
    public void testTimes() throws Exception {
        StringBinary stringBinary = new StringBinary("0000000000000000000000000000000000000000000000000000000000001000");
        StringBinary thatBinary =   new StringBinary("0000000000000000000000000000000000000000000000000000000000000100");
        StringBinary result = stringBinary.times(thatBinary);
        assertEquals(32, result.getAsInt());

        stringBinary = new StringBinary("0000000000000000000000000000000000000000000000000000000000001000");
        thatBinary =   new StringBinary("0000000000000000000000000000000000000000000000000000000000001000");
        result = stringBinary.times(thatBinary);
        assertEquals(64, result.getAsInt());
    }
}