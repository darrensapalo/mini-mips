package dlsu.advcarc.opcode;

import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;

/**
 * Created by user on 11/20/2015.
 */
public class ShiftOpcodeBuilder {

    private int rs;
    private int rd;
    private int shf;
    private String func;

    public ShiftOpcodeBuilder withRs(String rs) {
        if(rs == null)
            this.rs = 0;
        else
            this.rs = Integer.parseInt(rs.replaceAll("R", ""));

        return this;
    }

    public ShiftOpcodeBuilder withRd(String rd) {
        if(rd == null)
            this.rd = 0;
        else
            this.rd = Integer.parseInt(rd.replaceAll("R", ""));

        return this;
    }

    public ShiftOpcodeBuilder withShf(String shfHex) {

        String shfBin = RadixHelper.convertHexToStringBinary(shfHex).getBinaryValue();

        // Get only the last 5 bits
        if(shfBin.length() > 5)
            shfBin = shfBin.substring(shfBin.length()-5);

        this.shf = Integer.valueOf(shfBin, 2);

        return this;
    }

    public ShiftOpcodeBuilder withFunc(String func) {
        this.func = func;
        return this;
    }

    public Opcode build(){

        StringBuilder sb = new StringBuilder();

        sb.append("000000");
        sb.append("00000");
        sb.append(RadixHelper.convertLongToStringBinary(rs).padBinaryValue(5));
        sb.append(RadixHelper.convertLongToStringBinary(rd).padBinaryValue(5));
        sb.append(RadixHelper.convertLongToStringBinary(shf).padBinaryValue(5));
        sb.append(RadixHelper.convertLongToStringBinary(OpcodeHelper.getOpcodeNumber(func)).padBinaryValue(6));

        return new Opcode(new StringBinary(sb.toString()));
    }


}
