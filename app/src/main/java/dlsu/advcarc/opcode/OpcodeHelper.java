package dlsu.advcarc.opcode;

import dlsu.advcarc.parser.StringBinary;

/**
 * Created by user on 11/20/2015.
 */
public class OpcodeHelper {

    public static int getOpcodeNumber(String instruction) {
        switch (instruction.toUpperCase()) {
            case "DADDU":
                return 45;
            case "DMULT":
                return 28;
            case "OR":
                return 37;
            case "SLT":
                return 42;
            case "BEQ":
                return 4;
            case "LW":
                return 35;
            case "LWU":
                return 39;
            case "SW":
                return 32;
            case "DSLL":
                return 56;
            case "ANDI":
                return 12;
            case "DADDIU":
                return 25;
            case "J":
                return 2;
            case "L.S":
                return 49;
            case "S.S":
                return 57;
            case "ADD.S":
                return 0;
            case "MUL.S":
                return 2;
        }
        return -1;
    }

    public static String getInstructionType(StringBinary ir) {
        String binaryValue = ir.getBinaryValue();
        StringBinary opcode = new StringBinary(binaryValue.substring(0, 6));

        switch (opcode.getAsInt()) {
            // Jump
            case 2:
                return "J";

            // Regular R types
            case 0:
                return "R";

            // I Types
            case 4:
            case 35:
            case 39:
            case 43:
            case 12:
            case 25:
            case 49:
            case 57:
                return "I";

            // Extended R
            case 17:
                return "Rx";
        }
        return null;
    }

    public static String getInstruction(StringBinary ir) {
        String binaryValue = ir.getBinaryValue();

        if (binaryValue.length() == 64)
            binaryValue = binaryValue.substring(32, 64);
        StringBinary opcode = new StringBinary(binaryValue.substring(0, 6));
        StringBinary func = new StringBinary(binaryValue.substring(26, 32));

        switch (opcode.getAsInt()) {

            // Jump
            case 2:
                return "J";

            // Regular R types
            case 0:
                switch (func.getAsInt()) {
                    case 45:
                        return "DADDU";
                    case 28:
                        return "DMULT";
                    case 37:
                        return "OR";
                    case 42:
                        return "SLT";
                    case 56:
                        return "DSLL";
                }

                // I Types
            case 4:
                return "BEQ";
            case 35:
                return "LW";
            case 39:
                return "LWU";
            case 43:
                return "SW";
            case 12:
                return "ANDI";
            case 25:
                return "DADDIU";
            case 49:
                return "L.S";
            case 57:
                return "S.S";

            // Extended R
            case 17:
                switch (func.getAsInt()) {
                    case 0:
                        return "ADD.S";
                    case 2:
                        return "MUL.S";
                }
        }
        return null;
    }
}
