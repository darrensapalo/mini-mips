package dlsu.advcarc.cpu;

import dlsu.advcarc.immediate.register.Immediate;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.Register;

/**
 * Created by Darren on 11/21/2015.
 */
public class ALU {

    public static String executeCond(String instruction, Memory EXMEM_IR, Register a, Register b){
        StringBinary _a = a.getValue();
        StringBinary _b = b.getValue();

        switch (instruction){
            case "SLT":
                return (_a.getAsInt() < _b.getAsInt()) ? "1" : "0";
        }
        return null;
    }

    public static StringBinary executeALU(String instruction, Memory ir, Register a, Register b, Immediate imm, StringBinary _npc) {
        StringBinary _a = a.getValue();
        StringBinary _b = b.getValue();
        StringBinary _imm = imm.getValue();

        switch (instruction){
            case "DADDU":
                return _a.plus(_b);

            case "OR":
                return _a.or(_b);

            case "SLT":
                return null;

            case "DMULT":
                //TODO Check how to do this properly; result should be stored in HI and LO
                return _a.times(_b);

            case "DSLL":
                String shiftAmount = ir.getAsBinary().substring(21, 25);
                StringBinary shiftAmountBinary = new StringBinary(shiftAmount);
                return _a.shiftRight(shiftAmountBinary.getAsInt());

            case "ADD.S":
                // todo: how to read binary as floating point?
                return StringBinary.valueOf(_a.getAsDouble() + _b.getAsDouble());

            case "MUL.S":
                // todo: how to read binary as floating point?
                return StringBinary.valueOf(_a.getAsDouble() * _b.getAsDouble());

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
