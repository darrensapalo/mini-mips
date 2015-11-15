package dlsu.advcarc.parser;

import java.text.NumberFormat;

/**
 * Created by Darren on 11/9/2015.
 */
public interface Writable {

    /**
     * Interface method for writing a value into memory or a register
     *
     * @param value represented in binary
     * @throws NumberFormatException if you enter more than 64 bits
     */
    void write(String value) throws NumberFormatException;

    String read();

    void addDependency(Instruction instruction, Parameter.DependencyType type);

    Instruction peekDependency(Parameter.DependencyType type);
}
