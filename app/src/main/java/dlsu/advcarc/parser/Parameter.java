package dlsu.advcarc.parser;

import dlsu.advcarc.dependency.DependencyChecker;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.register.RegisterManager;

/**
 * Created by Darren on 11/9/2015.
 */
public class Parameter {
    private final Instruction instruction;
    private Writable parameter;

    public enum DependencyType {
        read, write
    }

    public Parameter(String param, Instruction instruction) {
        this.instruction = instruction;
        parameter = getParameter(param);

    }

    public void analyzeDependency(){
        DependencyType type = DependencyChecker.check(parameter, instruction);
        parameter.addDependency(instruction, type);
    }

    private Writable getParameter(String parameter){
        if (parameter.startsWith("R") || parameter.startsWith("F"))
            return RegisterManager.instance().getInstance(parameter);
        return MemoryManager.instance().getInstance(parameter);
    }

    public Instruction peekDependency(DependencyType type){
        return parameter.peekDependency(type);
    }

    public Writable getParameter() {
        return parameter;
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
