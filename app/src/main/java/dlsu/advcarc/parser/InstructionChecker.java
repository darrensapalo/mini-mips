package dlsu.advcarc.parser;

import java.util.ArrayList;
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

    public static String parseLabel(String mes) {
        if (mes == null)
            return null;

        Pattern pattern = Pattern.compile(MipsParser.LABEL_REGEX);
        Matcher matcher = pattern.matcher(mes);
        if (matcher.find())
            return matcher.group().replace(":", "");

        return null;
    }

    public static Instruction getInstruction(String mes){
        return new Instruction(mes);
    }

    public static String parseInstruction(String mes) {
        if (mes == null)
            return null;

        // trim
        mes = mes.trim();

        // Remove label if it exists
        String label = parseLabel(mes);
        if (label != null)
            mes = mes.substring(label.length() + 1);

        // trim
        mes = mes.trim();

        String[] split = mes.split(" ");
        if (split.length > 0)
            return split[0];

        return null;
    }

    public static ArrayList<Parameter> getParameters(String mes, Instruction instruction){
        mes = mes.trim();
        String label = parseLabel(mes);

        if (label != null)
            mes = mes.substring(label.length() + 1);

        String inst = parseInstruction(mes);
        if (inst == null) return null;

        mes = mes.trim();

        mes = mes.substring(inst.length()).trim();

        mes = mes.replace(";", "").replace(")", "");
        String[] split = mes.split("[ ,\\(]+");

        ArrayList<Parameter> params = new ArrayList<>();

        for (String param : split) {
            Parameter.ParameterType type;
            if (param.startsWith("R"))
                type = Parameter.ParameterType.register;
            else if (param.startsWith("F"))
                type = Parameter.ParameterType.register;
            else if (param.startsWith("#"))
                type = Parameter.ParameterType.immediate;
            else
                type = Parameter.ParameterType.memory;

            params.add(new Parameter(param, type, instruction));
        }

        return params;
    }

    public static boolean validateInstruction(String mes) {
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
