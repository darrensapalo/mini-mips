package dlsu.advcarc.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Darren on 11/6/2015.
 */
public class InstructionChecker {
    public InstructionChecker() {

    }

    public Instruction checkLine(String mes) {
        String[] split = mes.split("[ ,:]");
        return null;
    }

    public String getLabel(String mes) {
        if (mes == null)
            return null;

        Pattern pattern = Pattern.compile("([a-zA-Z_])*:");
        Matcher matcher = pattern.matcher(mes);
        if (matcher.find()) {
            return matcher.group().replace(":", "");
        }

        return null;
    }

    public String getInstruction(String mes) {


        if (mes == null)
            return null;

        // trim
        mes = mes.trim();

        // Remove label if it exists
        String label = getLabel(mes);
        if (label != null)
            mes = mes.substring(label.length() + 1);

        // trim
        mes = mes.trim();

        String[] split = mes.split(" ");
        if (split.length > 0)
            return split[0];

        return null;
    }

    public String[] getParameters(String mes){
        mes = mes.trim();
        String label = getLabel(mes);

        if (label != null)
            mes = mes.substring(label.length() + 1);

        String instruction = getInstruction(mes);
        if (instruction == null) return null;

        mes = mes.trim();

        mes = mes.substring(instruction.length()).trim();
        return mes.split("[ ,]+");
    }

    public boolean validateInstruction(String mes) {
        switch (mes.toUpperCase()) {
            case "DADDU":
            case "DMULT":
            case "OR":
            case "SLT":
            case "BEQ":
            case "LW":
            case "LWU":
            case "SW":
            case "DSLL":
            case "ANDI":
            case "DADDIU":
            case "J":
            case "L.S":
            case "S.S":
            case "ADD.S":
            case "MUL.S":
                return true;
            default:
                return false;
        }
    }
}
