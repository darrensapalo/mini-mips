package dlsu.advcarc.opcode;

import dlsu.advcarc.cpurevised.ExecutionManager;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;

/**
 * Created by user on 11/20/2015.
 */
public class OpcodeFactory {

    public static Opcode createOpcode(String lineOfCode, String codeAddressHex) {
        String[] tokens = splitCode(lineOfCode);

        switch (tokens[0]) {
            case "DADDU":
            case "OR":
            case "SLT":
                return new ROpcodeBuilder()
                        .withFunc(tokens[0])
                        .withRd(tokens[1])
                        .withRs(tokens[2])
                        .withRt(tokens[3])
                        .build();

            case "DMULT":
                return new ROpcodeBuilder()
                        .withFunc(tokens[0])
                        .withRs(tokens[1])
                        .withRt(tokens[2])
                        .build();

            case "DSLL":
                return new ShiftOpcodeBuilder()
                        .withFunc(tokens[0])
                        .withRd(tokens[1])
                        .withRs(tokens[2])
                        .withShf(tokens[3])
                        .build();


            case "ADD.S":
            case "MUL.S":
                return new ExtROpcodeBuilder()
                        .withFunc(tokens[0])
                        .withD(tokens[1])
                        .withS(tokens[2])
                        .withT(tokens[3])
                        .build();

            case "LW":
            case "LWU":
            case "SW":
            case "L.S":
            case "S.S":
                return new IOpcodeBuilder()
                        .withFunc(tokens[0])
                        .withRtd(tokens[1])
                        .withImm(RadixHelper.convertHexToStringBinary(tokens[2]))
                        .withRs(tokens[3])
                        .build();

            case "ANDI":
            case "DADDIU":

                return new IOpcodeBuilder()
                        .withFunc(tokens[0])
                        .withRtd(tokens[1])
                        .withRs(tokens[2])
                        .withImm(RadixHelper.convertHexToStringBinary(tokens[3]))
                        .build();

            case "J":

                String jLabelAddressHex = ExecutionManager.instance().getProgramCode().getHexAddressOfLabel(tokens[1]);
                StringBinary jLabelAddressBin = RadixHelper.convertHexToStringBinary(jLabelAddressHex);
                StringBinary jOffset = jLabelAddressBin.divide(StringBinary.valueOf(4));

                return new JOpcodeBuilder()
                        .withFunc(tokens[0])
                        .withOffset(jOffset)
                        .build();

            case "BEQ":

                String labelAddressHex = ExecutionManager.instance().getProgramCode().getHexAddressOfLabel(tokens[3]);
                StringBinary labelAddressBin = RadixHelper.convertHexToStringBinary(labelAddressHex);

                StringBinary codeAddressBin = RadixHelper.convertHexToStringBinary(codeAddressHex);
                StringBinary nextAddressBin = codeAddressBin.plus(StringBinary.valueOf(4));

                StringBinary offset = labelAddressBin.minus(nextAddressBin).divide(StringBinary.valueOf(4));

                return new IOpcodeBuilder()
                        .withFunc(tokens[0])
                        .withRs(tokens[1])
                        .withRtd(tokens[2])
                        .withImm(offset)
                        .build();

        }

        return null;
    }

    private static String[] splitCode(String lineOfCode) {
        return lineOfCode.split("\\s|\\s*[\\(\\),;]\\s*");
    }

}
