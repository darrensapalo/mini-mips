package dlsu.advcarc.parser;

/**
 * Created by Darren on 11/9/2015.
 */
public interface Writable {
    void write(String value);
    String read();
    void addDependency(Instruction instruction, Parameter.DependencyType type);
    Instruction peekDependency(Parameter.DependencyType type);
}
