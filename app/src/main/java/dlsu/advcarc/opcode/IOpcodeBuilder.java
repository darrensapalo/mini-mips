package dlsu.advcarc.opcode;

import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;

/**
 * Created by user on 11/20/2015.
 */
public class IOpcodeBuilder {

    private int rs;
    private int rtd;
    private StringBinary imm;
    private String func;

    public IOpcodeBuilder withRs(String rs) {
        if(rs == null)
            this.rs = 0;
        else
            this.rs = Integer.parseInt(rs.replaceAll("[RF]", ""));

        return this;
    }

    public IOpcodeBuilder withRtd(String rtd) {
        if(rtd == null)
            this.rtd = 0;
        else
            this.rtd = Integer.parseInt(rtd.replaceAll("[RF]", ""));

        return this;
    }

    public IOpcodeBuilder withImm(StringBinary imm) {
        this.imm = imm;
        return this;
    }

    public IOpcodeBuilder withFunc(String func) {
        this.func = func;
        return this;
    }

    public Opcode build(){

        StringBuilder sb = new StringBuilder();
        sb.append(RadixHelper.convertLongToStringBinary(OpcodeHelper.getOpcodeNumber(func)).padBinaryValue(6));
        sb.append(RadixHelper.convertLongToStringBinary(rs).padBinaryValue(5));
        sb.append(RadixHelper.convertLongToStringBinary(rtd).padBinaryValue(5));
        sb.append(imm.forceLength(16));

        return new Opcode(new StringBinary(sb.toString()));
    }


}
