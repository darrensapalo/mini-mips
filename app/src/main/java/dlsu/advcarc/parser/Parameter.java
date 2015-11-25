package dlsu.advcarc.parser;

import dlsu.advcarc.dependency.DependencyChecker;
import dlsu.advcarc.immediate.register.Immediate;
import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.register.RegisterManager;

/**
 * Created by Darren on 11/9/2015.
 */
public class Parameter {
    private final Instruction instruction;
    private Writable parameter;
    private ParameterType parameterType;
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

    public Parameter(String param, ParameterType parameterType, Instruction instruction) {
        this.instruction = instruction;
        this.parameterType = parameterType;
        parameter = getParameter(param, parameterType);
    }

    public DependencyType analyzeDependency() {
        dependencyType = DependencyChecker.check(parameter, instruction);
        return dependencyType;
    }

    public void addDependency() {
        parameter.addDependency(instruction, dependencyType);
    }

    private Writable getParameter(String parameter, ParameterType type) {
        if (type == ParameterType.register)
            if (parameter.startsWith("R") || parameter.startsWith("F"))
                return RegisterManager.instance().getInstance(parameter);

        if (type == ParameterType.memory)
            return MemoryManager.instance().getInstance(parameter);

        parameter = parameter.replace("#", "");
        return new Immediate(new StringBinary(parameter));
    }

    public Instruction peekDependency() {
        return parameter.peekDependency(DependencyType.write);
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

    public ParameterType getParameterType() {
        return parameterType;
    }

    public DependencyType getDependencyType() {
        return dependencyType;
    }

    public String getValue() {
        if (parameter == null)
            return "null";

        return parameter.read().toHexString();
    }

}
