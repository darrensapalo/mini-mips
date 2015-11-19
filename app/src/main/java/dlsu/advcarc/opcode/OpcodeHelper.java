package dlsu.advcarc.opcode;

/**
 * Created by user on 11/20/2015.
 */
public class OpcodeHelper {

    public static int getOpcodeNumber(String instruction){
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
}
