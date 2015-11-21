package dlsu.advcarc.cpu;

import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.StringBinary;

/**
 * Created by Darren on 11/21/2015.
 */
public class ALU {

    public static String executeCond(String instruction, Memory EXMEM_IR, Memory a, Memory b){
        StringBinary _a = new StringBinary(a.getAsBinary());
        StringBinary _b = new StringBinary(b.getAsBinary());

        switch (instruction){
            case "SLT":
                return (_a.getAsInt() < _b.getAsInt()) ? "1" : "0";
        }
        return null;
    }

    public static StringBinary executeALU(String instruction, Memory ir, Memory a, Memory b, Memory imm, StringBinary _npc) {
        StringBinary _a = new StringBinary(a.getAsBinary());
        StringBinary _b = new StringBinary(b.getAsBinary());
        StringBinary _imm = new StringBinary(imm.getAsBinary());

        switch (instruction){
            case "DADDU":
                return _a.plus(_b);

            case "OR":
                return _a.or(_b);

            case "SLT":
                return null;

            case "DMULT":
                return _a.times(_b);

            case "DSLL":
                String shiftAmount = ir.getAsBinary().substring(21, 25);
                StringBinary shiftAmountBinary = new StringBinary(shiftAmount);
                return _a.shiftRight(shiftAmountBinary.getAsInt());

            case "ADD.S":
                return _a.plus(_b);

            case "MUL.S":
                return _a.times(_b);

            case "LW":
            case "LWU":
            case "SW":
            case "L.S":
            case "S.S":
                // Load store instructions are 'memory references'
                // ALUOutput = A + Imm
                return _a.plus(_imm);

            case "ANDI":
                return _a.and(_imm);

            case "DADDIU":
                return _a.plus(_imm);

            case "J":
                return _imm.shiftRight(-2);

            case "BEQ":
                return _npc.plus(_imm.shiftRight(-2));
        }
        return null;
    }
}
