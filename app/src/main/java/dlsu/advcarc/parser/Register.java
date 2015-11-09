package dlsu.advcarc.parser;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Darren on 11/9/2015.
 */
public class Register implements Writable{
    private static HashMap<String, Register> cache = new HashMap<>();
    private String register;

    public LinkedList<Instruction> writeDependency = new LinkedList<>();
    public LinkedList<Instruction> readDependency = new LinkedList<>();

    public Register(String register) {
        this.register = register;
    }

    public static Register getInstance(String register) throws IllegalArgumentException {
        validateRegister(register);
        Register reg = cache.get(register);
        if (reg != null) return reg;

        reg = new Register(register);
        cache.put(register, reg);
        return reg;
    }

    public static boolean validateRegister(String register) throws IllegalArgumentException {
        return true;
    }

    @Override
    public void write(String value) {

    }

    @Override
    public String read() {
        return null;
    }

    @Override
    public String toString() {
        return register;
    }

    @Override
    public void addDependency(Instruction instruction, Parameter.DependencyType type) {
        switch (type) {
            case read:
                readDependency.add(instruction);
                break;

            case write:
                writeDependency.add(instruction);
                break;
        }
    }

    @Override
    public Instruction peekDependency(Parameter.DependencyType type) {
        switch (type) {
            case read:
                return readDependency.peek();

            case write:
                return writeDependency.peek();
        }
        return null;
    }

    public static void clear(){
        cache.clear();
    }
}
