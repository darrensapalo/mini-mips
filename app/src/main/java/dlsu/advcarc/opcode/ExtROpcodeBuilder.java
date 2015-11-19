package dlsu.advcarc.opcode;

import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;

/**
 * Created by user on 11/20/2015.
 */
public class ExtROpcodeBuilder {

    private int t;
    private int s;
    private int d;
    private String func;

    public ExtROpcodeBuilder withS(String s) {
        if(s == null)
            this.s = 0;
        else
            this.s = Integer.parseInt(s.replaceAll("F", ""));

        return this;
    }

    public ExtROpcodeBuilder withT(String t) {
        if(t == null)
            this.t = 0;
        else
            this.t = Integer.parseInt(t.replaceAll("F", ""));

        return this;
    }

    public ExtROpcodeBuilder withD(String d) {
        if(d == null)
            this.d = 0;
        else
            this.d = Integer.parseInt(d.replaceAll("F", ""));

        return this;
    }

    public ExtROpcodeBuilder withFunc(String func) {
        this.func = func;
        return this;
    }

    public Opcode build(){

        StringBuilder sb = new StringBuilder();
        sb.append(RadixHelper.convertLongToStringBinary(17).padBinaryValue(6));
        sb.append(RadixHelper.convertLongToStringBinary(16).padBinaryValue(5));
        sb.append(RadixHelper.convertLongToStringBinary(t).padBinaryValue(5));
        sb.append(RadixHelper.convertLongToStringBinary(s).padBinaryValue(5));
        sb.append(RadixHelper.convertLongToStringBinary(d).padBinaryValue(5));
        sb.append(RadixHelper.convertLongToStringBinary(OpcodeHelper.getOpcodeNumber(func)).padBinaryValue(6));

        return new Opcode(new StringBinary(sb.toString()));
    }


}
