package dlsu.advcarc.parser;

import dlsu.advcarc.opcode.OpcodeHelper;

import java.util.ArrayList;

/**
 * Created by Darren on 11/6/2015.
 */
public class Instruction {
    private int cycle;
    private String input;
    private String instruction;
    private String label;
    private Stage stage;
    private ArrayList<Parameter> parameters = new ArrayList<>();

    // generate instruction based on binary
    public Instruction(StringBinary binary, String lineOfCode, int cycle) {
        this.cycle = cycle;
        String binaryValue = binary.getBinaryValue();

        if (binaryValue.length() == 64) {
            binaryValue = binaryValue.substring(32, 64);
            binary = new StringBinary(binaryValue);
        }

        instruction = lineOfCode;
        String instructionType = OpcodeHelper.getInstructionType(binary);
        switch (instructionType) {
            case "J":
                String label = binary.getBinaryValue().substring(5, 31);
                StringBinary stringBinary = new StringBinary(label);
                String hex = stringBinary.toHexString();
                Parameter parameter = new Parameter(hex, Parameter.ParameterType.immediate, this);
                parameters.add(parameter);
                break;

            case "R":
                String rs = binary.getBinaryValue().substring(6, 11);
                StringBinary binary_rs = new StringBinary(rs);
                Parameter parameter_rs = new Parameter("R" + binary_rs.getAsInt(), Parameter.ParameterType.register, this);
                parameters.add(parameter_rs);

                String rt = binary.getBinaryValue().substring(11, 16);
                StringBinary binary_rt = new StringBinary(rt);
                Parameter parameter_rt = new Parameter("R" + binary_rt.getAsInt(), Parameter.ParameterType.register, this);
                parameters.add(parameter_rt);

                String rd = binary.getBinaryValue().substring(16, 21);
                StringBinary binary_rd = new StringBinary(rd);
                Parameter parameter_rd = new Parameter("R" + binary_rd.getAsInt(), Parameter.ParameterType.register, this);
                parameters.add(parameter_rd);
                break;

            case "I":
                // GPR or FPR?

                String parameterTypes = "R";
                switch (instruction) {
                    case "BEQ":
                    case "LW":
                    case "LWU":
                    case "SW":
                    case "ANDI":
                    case "DADDIU":
                        parameterTypes = "R";
                        break;

                    case "L.S":
                    case "S.S":
                        parameterTypes = "F";
                        break;
                }
                String irs = binary.getBinaryValue().substring(6, 11);
                StringBinary binary_irs = new StringBinary(irs);
                Parameter parameter_irs = new Parameter(parameterTypes + binary_irs.getAsInt(), Parameter.ParameterType.register, this);
                parameters.add(parameter_irs);

                String irt = binary.getBinaryValue().substring(11, 16);
                StringBinary binary_irt = new StringBinary(irt);
                Parameter parameter_irt = new Parameter(parameterTypes + binary_irt.getAsInt(), Parameter.ParameterType.register, this);
                parameters.add(parameter_irt);

                String iimm = binary.getBinaryValue().substring(16, 31);
                Parameter parameter_iimm = new Parameter(iimm, Parameter.ParameterType.immediate, this);
                parameters.add(parameter_iimm);
                break;

            case "Rx":
                // Always FPR because instruction set is limited to ADD.S and MUL.S
                String rxs = binary.getBinaryValue().substring(11, 16);
                StringBinary binary_rxs = new StringBinary(rxs);
                Parameter parameter_rxs = new Parameter("F" + binary_rxs.getAsInt(), Parameter.ParameterType.register, this);
                parameters.add(parameter_rxs);

                String rxt = binary.getBinaryValue().substring(16, 21);
                StringBinary binary_rxt = new StringBinary(rxt);
                Parameter parameter_rxt = new Parameter("F" + binary_rxt.getAsInt(), Parameter.ParameterType.register, this);
                parameters.add(parameter_rxt);

                String rxd = binary.getBinaryValue().substring(21, 26);
                StringBinary binary_rxd = new StringBinary(rxd);
                Parameter parameter_rxd = new Parameter("F" + binary_rxd.getAsInt(), Parameter.ParameterType.register, this);
                parameters.add(parameter_rxd);
                break;

        }


    }

    public int getExCycles() {
        return 4;  // compute number of ex cycles
    }

    public boolean isBranch() {
        return "BEQ".equals(getInstructionOnly().toUpperCase());
    }

    public String getExecutionType() {
        switch (getInstructionOnly()){
            case "ADD.S":
                return "ADDER";
            case "MUL.S":
                return "MULTIPLIER";
            default:
                return "INTEGER";
        }
    }

    public enum Stage {
        IF, ID, EX, MEM, WB, DONE
    }

    public Instruction(String line) {
        if (line == null)
            throw new IllegalArgumentException("Cannot create an instruction that is null.");
        this.input = line;
        this.label = InstructionChecker.parseLabel(line);
        this.instruction = InstructionChecker.parseInstruction(line);
        this.parameters = InstructionChecker.getParameters(line, this);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public String toString() {
        return instruction;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getInstructionOnly() {
        if (instruction != null)
            return instruction.split(" ")[0].toUpperCase();
        return instruction;
    }

    public Stage getStage() {
        return stage;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public int getCycle() {
        return cycle;
    }
}
