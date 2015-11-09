package dlsu.advcarc.parser;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Darren on 11/9/2015.
 */
public class Memory implements Writable {
    private static HashMap<String, Memory> cache = new HashMap<>();
    private String memory;

    public LinkedList<Instruction> writeDependency = new LinkedList<>();
    public LinkedList<Instruction> readDependency = new LinkedList<>();

    public Memory(String memory) {
        this.memory = memory;
    }

    public static Memory getInstance(String memory) throws IllegalArgumentException {
        validateMemory(memory);
        Memory mem = cache.get(memory);
        if (mem != null) return mem;

        mem = new Memory(memory);
        cache.put(memory, mem);
        return mem;
    }

    public static boolean validateMemory(String memory) throws IllegalArgumentException {
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
        return memory;
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

    public static void clear() {
        cache.clear();
    }
}
