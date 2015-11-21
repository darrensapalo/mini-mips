package dlsu.advcarc.register;

import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.parser.Writable;
import dlsu.advcarc.utils.RadixHelper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;

/**
 * Created by Darren on 11/9/2015.
 */
public class Register implements Writable {
    private String register;
    private StringBinary value = new StringBinary("0");

    public LinkedList<Instruction> writeDependency = new LinkedList<>();
    public LinkedList<Instruction> readDependency = new LinkedList<>();

    public Register(String register) {
        this.register = register;
    }

    public static String getType(String registerName) {
        return registerName.charAt(0) + "";
    }

    public static int getNumber(String registerName) {
        return Integer.parseInt(registerName.substring(1));
    }

    public static boolean validate(String register) throws IllegalArgumentException {
        return register.matches("(R|F)[0-9]{1,2}");
    }

    public StringBinary getValue() {
        return value;
    }

    public void setValue(StringBinary value) {
        this.value = value;
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("name", register);
        jsonObject.put("value", RadixHelper.padWithZero(value.toHexString(), 16));
        return jsonObject;
    }

    @Override
    public void write(String value) {

    }

    @Override
    public StringBinary read() {
        return value;
    }

    @Override
    public String toString() {
        return register;
    }

    @Override
    public void addDependency(Instruction instruction, Parameter.DependencyType type) {
        switch (type) {
            case read:
                if (!readDependency.contains(instruction))
                    readDependency.add(instruction);
                break;

            case write:
                if (!writeDependency.contains(instruction))
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


    @Override
    public void dequeueDependency(Instruction instruction) {
        if (instruction.equals(peekDependency(Parameter.DependencyType.read))) {
            readDependency.remove(instruction);
        } else {
            System.err.println("Trying to dequeue when i am not at the head of the queue!");
        }

        if (instruction.equals(peekDependency(Parameter.DependencyType.write))) {
            writeDependency.remove(instruction);
        } else {
            System.err.println("Trying to dequeue when i am not at the head of the queue!");
        }
    }
}
