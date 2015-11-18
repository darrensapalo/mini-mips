package dlsu.advcarc.dependency;

import dlsu.advcarc.parser.*;
import dlsu.advcarc.register.Register;

import java.util.ArrayList;

/**
 * Created by Darren on 11/10/2015.
 */
public class DependencyChecker {

    // todo: find out type of dependency
    public static Parameter.DependencyType check(Writable parameter, Instruction instruction) {
        ArrayList<Parameter> parameters = instruction.getParameters();
        switch (instruction.getInstruction()){
            // R types
            case "DADDU":
            case "DMULT":
            case "OR":
            case "SLT":
                Register parameter1 = (Register) parameters.get(0).getParameter();
                Register parameter2 = (Register) parameters.get(1).getParameter();
                Register parameter3 = (Register) parameters.get(2).getParameter();
                if (parameter.equals(parameter1))
                    return Parameter.DependencyType.write;

                return Parameter.DependencyType.read;

            case "J":
                return Parameter.DependencyType.read;

                // I types
            case "BEQ":
            case "LW":
            case "LWU":
            case "SW":
            case "DSLL":
            case "ANDI":
            case "DADDIU":
                parameter1 = (Register) parameters.get(0).getParameter();
                parameter2 = (Register) parameters.get(1).getParameter();
                Memory memory3 = (Memory) parameters.get(2).getParameter();
                if (parameter.equals(parameter1))
                    return Parameter.DependencyType.write;

                return Parameter.DependencyType.read;

            case "ADD.S":
            case "MUL.S":

                parameter1 = (Register) parameters.get(0).getParameter();
                Memory memory2 = (Memory) parameters.get(1).getParameter();
                parameter3 = (Register) parameters.get(2).getParameter();
                if (parameter.equals(parameter1))
                    return Parameter.DependencyType.write;
                return Parameter.DependencyType.read;


            case "L.S":
            case "S.S":
                // todo: analyze based on parameter
        }
        return Parameter.DependencyType.write;
    }
}
