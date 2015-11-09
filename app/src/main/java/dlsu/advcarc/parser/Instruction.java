package dlsu.advcarc.parser;

import java.util.ArrayList;

/**
 * Created by Darren on 11/6/2015.
 */
public class Instruction {
    private String input;
    private String instruction;
    private Stage stage;
    private ArrayList<Parameter> parameters = new ArrayList<>();

    public enum Stage {
        IF, ID, EX, MEM, WB
    }

    public Instruction(String line) {
        this.input = line;
        this.instruction = InstructionChecker.parseInstruction(line);
        this.parameters = InstructionChecker.getParameters(line, this);

    }

    @Override
    public String toString() {
        return input;
    }

    public String getInstruction() {
        return instruction;
    }

    public Stage getStage() {
        return stage;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }
}
