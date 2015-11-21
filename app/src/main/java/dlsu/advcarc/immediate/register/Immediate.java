package dlsu.advcarc.immediate.register;

import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.parser.Writable;

/**
 * Created by Darren on 11/9/2015.
 */
public class Immediate implements Writable {

    private StringBinary value = new StringBinary("0");

    public Immediate(StringBinary value) {
        this.value = value;
    }

    public StringBinary getValue(){
        return value;
    }

    @Override
    public void write(String value) {
        throw new IllegalStateException("Cannot write on immediate!");
    }

    @Override
    public StringBinary read() {
        return value;
    }

    @Override
    public String toString() {
        return value.getBinaryValue();
    }

    @Override
    public void addDependency(Instruction instruction, Parameter.DependencyType type) {

    }

    @Override
    public Instruction peekDependency(Parameter.DependencyType type) {
        return null;
    }

    @Override
    public void dequeueDependency(Instruction instruction) {

    }
}
