package dlsu.advcarc.cpu;

import dlsu.advcarc.immediate.register.Immediate;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.Register;
import dlsu.advcarc.utils.RadixHelper;

/**
 * Created by Darren on 11/21/2015.
 */
public class ALU {

    public static String executeCond(String instruction, Memory EXMEM_IR, Parameter a, Parameter b){
        if(instruction.equals("J"))
            return "1";

        StringBinary _a = a.getParameter().read();
        StringBinary _b = b.getParameter().read();

        switch (instruction){
            case "J":
                return "1";

            case "BEQ":
                if (_a.getBinaryValue().equals(_b.getBinaryValue()))
                    return "1";

            default:
                return "0";
        }
    }

    public static StringBinary executeALU(String instruction, Memory ir, Parameter a, Parameter b, Parameter imm, StringBinary _npc) {

        if(instruction.equals("J")) {
            String jInstructionBinary = new StringBinary(ir.getAsBinary()).forceLength(32);
            StringBinary targetAddressDiv4Binary = new StringBinary(jInstructionBinary.substring(6, 32));
            return targetAddressDiv4Binary.times(StringBinary.valueOf(4));
        }

        StringBinary _a = a.getParameter().read();
        StringBinary _b = b.getParameter().read();
        StringBinary _imm = imm.getParameter().read();

        switch (instruction){
            case "DADDU":
                return _a.plus(_b);

            case "OR":
                return _a.or(_b);

            case "SLT":
                return (_a.getAsInt() < _b.getAsInt()) ? StringBinary.valueOf(1) : StringBinary.valueOf(0);

            case "DMULT":
                //TODO Check how to do this properly; result should be stored in HI and LO
                return _a.times(_b);

            case "DSLL":
                String shiftAmount = new StringBinary(ir.getAsBinary()).forceLength(32).substring(21, 26);
                StringBinary shiftAmountBinary = new StringBinary(shiftAmount);
                return _a.shiftRight(-1*shiftAmountBinary.getAsInt());

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



            case "BEQ":
                StringBinary tempImm = new StringBinary(RadixHelper.padArithmetic(_imm.forceLength(16), 64));
                return _npc.plus(tempImm.times(StringBinary.valueOf(4)));
        }
        return null;
    }
}
