package dlsu.advcarc.memory;

import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.parser.Writable;
import dlsu.advcarc.utils.RadixHelper;
import io.vertx.core.json.JsonObject;

import java.util.LinkedList;

/**
 * Created by Darren on 11/9/2015.
 */
public class Memory implements Writable {
    private String memory;
    private StringBinary value = new StringBinary("0");

    public LinkedList<Instruction> writeDependency = new LinkedList<>();
    public LinkedList<Instruction> readDependency = new LinkedList<>();

    public Memory(String memory) {
        this.memory = memory;
    }

    public static boolean validate(String memory) throws IllegalArgumentException {
        return memory.matches("[0-9A-Fa-f]{4}") && Integer.valueOf(memory, 16) % 4 == 0;
    }

    public void setValue(StringBinary value) {
        this.value = value;
    }

    @Override
    public void write(String value) {
        MemoryManager.instance().updateMemory(memory, new StringBinary(value));
    }

    @Override
    public StringBinary read() {
        return value;
    }

    @Override
    public String toString() {
        return memory + ": " + getAsHex();
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("address", memory);
        jsonObject.put("value", RadixHelper.forceLength(getAsHex(), 8));
        return jsonObject;
    }

    public String getAsBinary() {
        return value.getBinaryValue();
    }

    public long getAsLong() {
        return value.getAsLong();
    }

    public double getAsDouble() {
        return value.getAsDouble();
    }

    public String getAsHex() {
        return value.toHexString();
    }

    @Override
    public void addDependency(Instruction instruction, Parameter.DependencyType type) {
        switch (type) {
            case read:
                if (!readDependency.contains(instruction))
                    readDependency.add(instruction);
                break;

            case write:
                if (!writeDependency.contains(instruction)) {
                    System.out.println("Adding a write dependency on " + this);
                    writeDependency.add(instruction);
                }
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

    @Override
    public void dequeueDependency(Instruction instruction) {
        if (instruction.equals(peekDependency(Parameter.DependencyType.read))) {
            readDependency.remove(instruction);
        }

        if (instruction.equals(peekDependency(Parameter.DependencyType.write))) {
            System.out.println("Removing write dependency on " + this);
            writeDependency.remove(instruction);
        }
    }
}
