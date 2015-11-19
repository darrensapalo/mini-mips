package dlsu.advcarc.opcode;

import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;

/**
 * Created by user on 11/20/2015.
 */
public class JOpcodeBuilder {

    private StringBinary offset;
    private String func;

    public JOpcodeBuilder withOffset(StringBinary offset){
        this.offset = offset;
        return this;
    }

    public JOpcodeBuilder withFunc(String func) {
        this.func = func;
        return this;
    }

    public Opcode build(){

        StringBuilder sb = new StringBuilder();
        sb.append(RadixHelper.convertLongToStringBinary(OpcodeHelper.getOpcodeNumber(func)).padBinaryValue(6));
        sb.append(offset.padBinaryValue(26));

        return new Opcode(new StringBinary(sb.toString()));
    }

}
