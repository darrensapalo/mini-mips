package dlsu.advcarc.parser;

/**
 * Created by Darren on 11/9/2015.
 */
public class Parameter {
    private Writable parameter;

    public enum DependencyType {
        read, write
    }

    public Parameter(String param, Instruction instruction) {
        parameter = getParameter(param);
        // todo: find out type of dependency
        parameter.addDependency(instruction, DependencyType.read);
    }

    private Writable getParameter(String parameter){
        if (parameter.startsWith("R"))
            return Register.getInstance(parameter);
        return Memory.getInstance(parameter);
    }

    public Instruction peekDependency(DependencyType type){
        return parameter.peekDependency(type);
    }

    @Override
    public String toString() {
        return parameter.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Parameter) {
            Parameter param = (Parameter) obj;
            return parameter.equals(param.parameter);
        }
        return parameter.equals(obj);
    }
}
