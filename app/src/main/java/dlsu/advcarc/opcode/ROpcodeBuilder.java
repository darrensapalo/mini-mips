package dlsu.advcarc.opcode;

import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;

/**
 * Created by user on 11/20/2015.
 */
public class ROpcodeBuilder {

    private int rs;
    private int rt;
    private int rd;
    private String func;


    public ROpcodeBuilder withRs(String rs) {
        if(rs == null)
            this.rs = 0;
        else
            this.rs = Integer.parseInt(rs.replaceAll("R", ""));

        return this;
    }

    public ROpcodeBuilder withRt(String rt) {
        if(rt == null)
            this.rt = 0;
        else
            this.rt = Integer.parseInt(rt.replaceAll("R", ""));

        return this;
    }

    public ROpcodeBuilder withRd(String rd) {
        if(rd == null)
            this.rd = 0;
        else
            this.rd = Integer.parseInt(rd.replaceAll("R", ""));

        return this;
    }

    public ROpcodeBuilder withFunc(String func) {
        this.func = func;
        return this;
    }

    public Opcode build(){

        StringBuilder sb = new StringBuilder();
        sb.append("000000");
        sb.append(RadixHelper.convertLongToStringBinary(rs).padBinaryValue(5));
        sb.append(RadixHelper.convertLongToStringBinary(rt).padBinaryValue(5));
        sb.append(RadixHelper.convertLongToStringBinary(rd).padBinaryValue(5));
        sb.append("00000");
        sb.append(RadixHelper.convertLongToStringBinary(OpcodeHelper.getOpcodeNumber(func)).padBinaryValue(6));

        return new Opcode(new StringBinary(sb.toString()));
    }
}
