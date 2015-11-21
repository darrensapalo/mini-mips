package dlsu.advcarc.parser;

import dlsu.advcarc.dependency.DependencyChecker;
import dlsu.advcarc.immediate.register.Immediate;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.register.RegisterManager;
import dlsu.advcarc.utils.RadixHelper;

/**
 * Created by Darren on 11/9/2015.
 */
public class Parameter {
    private final Instruction instruction;
    private Writable parameter;
    private ParameterType type;
    private DependencyType dependencyType;

    public void dequeueDependency() {
        parameter.dequeueDependency(instruction);


    }

    public enum ParameterType {
        register, memory, immediate
    }

    public enum DependencyType {
        read, write
    }

    public Parameter(String param, ParameterType type, Instruction instruction) {
        this.instruction = instruction;
        this.type = type;
        parameter = getParameter(param, type);
    }

    public void analyzeDependency(){
        DependencyType type = DependencyChecker.check(parameter, instruction);
        dependencyType = type;
        parameter.addDependency(instruction, type);
    }

    private Writable getParameter(String parameter, ParameterType type){
        if (type == ParameterType.register)
            if (parameter.startsWith("R") || parameter.startsWith("F"))
                return RegisterManager.instance().getInstance(parameter);

        if (type == ParameterType.memory)
            return MemoryManager.instance().getInstance(parameter);

        parameter = parameter.replace("#", "");
        return new Immediate(new StringBinary(parameter));
    }

    public Instruction peekDependency(){
        return parameter.peekDependency(getDependencyType());
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

    public DependencyType getDependencyType() {
        return dependencyType;
    }
}
