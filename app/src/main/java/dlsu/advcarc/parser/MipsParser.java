package dlsu.advcarc.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Darren on 11/6/2015.
 */
public class MipsParser {

    public static final String R_REGISTER_REGEX = "R([0-9]|[1-2][0-9]|[3][0-1])";
    public static final String F_REGISTER_REGEX = "F([0-9]|[1-2][0-9]|[3][0-1])";


    public static final String IMM_REGEX = "[0-9]{4}";
    public static final String LABEL_REGEX = "L[0-9]+:";

    public static ProgramCode parseCodeString(String codeString) throws Exception {
        Scanner scanner = new Scanner(codeString);
        ProgramCode programCode = new ProgramCode(codeString);

        while(scanner.hasNext()) {

            String rawLineOfCode = scanner.nextLine();
            String lineOfCode = rawLineOfCode.replaceAll("[\\(\\),;]"," ");

            String rawParsingErrors = validateLineOfCode(lineOfCode);

            // Stop parsing if errors were encountered.
            if(!rawParsingErrors.isEmpty()){
                String modifiedParsingErrors = "Invalid line ("+rawLineOfCode+") - "+rawParsingErrors;
                programCode.setParsingErrors(modifiedParsingErrors);
                break;
            }

            // if line is valid, add to the programcode
            programCode.addInstruction(rawLineOfCode);
        }


        //loop through each code, then call buildOpcode(ProgramCode) so they can look for labels if needed
        // TODO
        return programCode;

    }

    private static String validateLineOfCode(String line){
        Scanner scanner = new Scanner(line);
        while(scanner.hasNext()){

            String currInstruction = scanner.next();
            String label = "";

            if(currInstruction.matches(LABEL_REGEX)){
                label = currInstruction;
                currInstruction = scanner.next();
            }

            //validate instruction
            if(!validateInstruction(currInstruction)){
                return "Unrecognized instruction "+currInstruction;
            }

            // get list of parameter regexes that have to be matched
            List<String> paramRegexes = getParameterRegexes(currInstruction);

            // for each of the tokens, check if they match the regex
            for(String paramRegex: paramRegexes){

                if(!scanner.hasNext()){
                    return "Incomplete parameters";
                }

                String currString = scanner.next();
                if(!currString.matches(paramRegex)){
                    return "Unrecognized parameter "+currString;
                }
            }
        }

        return "";
    }

    private static List<String> getParameterRegexes(String instruction){

        List<String> regexes = new ArrayList<String>();

        switch(instruction) {
            case "DADDU":
            case "OR":
            case "SLT":
                regexes.add(R_REGISTER_REGEX);
                regexes.add(R_REGISTER_REGEX);
                regexes.add(R_REGISTER_REGEX);
                break;

            case "DMULT":
                regexes.add(R_REGISTER_REGEX);
                regexes.add(R_REGISTER_REGEX);
                break;


            case "DSLL":
                regexes.add(R_REGISTER_REGEX);
                regexes.add(R_REGISTER_REGEX);
                regexes.add(IMM_REGEX);
                break;

            case "ADD.S":
            case "MUL.S":
                regexes.add(F_REGISTER_REGEX);
                regexes.add(F_REGISTER_REGEX);
                regexes.add(F_REGISTER_REGEX);
                break;

            case "LW":
            case "LWU":
            case "SW":
                regexes.add(R_REGISTER_REGEX);
                regexes.add(IMM_REGEX);
                regexes.add(R_REGISTER_REGEX);
                break;

            case "L.S":
            case "S.S":
                regexes.add(F_REGISTER_REGEX);
                regexes.add(IMM_REGEX);
                regexes.add(R_REGISTER_REGEX);
                break;

            case "ANDI":
            case "DADDIU":
                regexes.add(R_REGISTER_REGEX);
                regexes.add(R_REGISTER_REGEX);
                regexes.add(IMM_REGEX);
                break;

            case "BEQ":
                regexes.add(R_REGISTER_REGEX);
                regexes.add(R_REGISTER_REGEX);
                regexes.add(LABEL_REGEX);

                break;

            case "J":
                regexes.add(LABEL_REGEX);
                break;
        }
        return regexes;
    }

    private static boolean validateInstruction(String instruction){

        switch(instruction){
            case "DADDU":
            case "OR":
            case "SLT":
            case "DMULT":
            case "DSLL":
            case "ADD.S":
            case "MUL.S":
            case "BEQ":
            case "LW":
            case "LWU":
            case "SW":
            case "L.S":
            case "S.S":
            case "ANDI":
            case "DADDIU":
                return  true;

            default: return false;
        }
    }
}
