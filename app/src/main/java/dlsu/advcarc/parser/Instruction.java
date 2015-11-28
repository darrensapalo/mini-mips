package dlsu.advcarc.parser;

import dlsu.advcarc.cpu.block.DataDependencyManager;
import dlsu.advcarc.dependency.DataDependencyException;
import dlsu.advcarc.opcode.OpcodeHelper;
import dlsu.advcarc.register.Register;
import dlsu.advcarc.utils.RadixHelper;

import java.util.ArrayList;

/**
 * Created by Darren on 11/6/2015.
 */
public class Instruction {
    private int cycle;
    private String input;
    private String instruction;
    private String label;
    private String memAddressHex;
    private Stage stage;
    private ArrayList<Parameter> parameters = new ArrayList<>();

    // generate instruction based on binary
    public Instruction(StringBinary binary, String lineOfCode, int cycle, String memAddressHex) {
        this.cycle = cycle;
        String binaryValue = binary.getBinaryValue();

        if (binaryValue.length() == 64) {
            binaryValue = binaryValue.substring(32, 64);
            binary = new StringBinary(binaryValue);
        }
        this.memAddressHex = memAddressHex;
        instruction = lineOfCode;
        String instructionType = OpcodeHelper.getInstructionType(binary);
        switch (instructionType) {
            case "J":
                String label = binary.getBinaryValue().substring(6, 32);
                StringBinary stringBinary = new StringBinary(label);
                Parameter parameter = new Parameter(stringBinary.getBinaryValue(), Parameter.ParameterType.immediate, this);
//                parameters.add(null);
//                parameters.add(null);
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
                switch (getInstructionOnly()) {
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

                if("DSLL".equals(OpcodeHelper.getInstruction(binary))){

                    String irt = binary.getBinaryValue().substring(11, 16);
                    StringBinary binary_irt = new StringBinary(irt);
                    Parameter parameter_irt = new Parameter(parameterTypes + binary_irt.getAsInt(), Parameter.ParameterType.register, this);
                    parameters.add(parameter_irt);

                    String irs = binary.getBinaryValue().substring(16, 21);
                    StringBinary binary_irs = new StringBinary(irs);
                    Parameter parameter_irs = new Parameter(parameterTypes + binary_irs.getAsInt(), Parameter.ParameterType.register, this);
                    parameters.add(parameter_irs);
                }
                else {
                    String irs = binary.getBinaryValue().substring(6, 11);
                    StringBinary binary_irs = new StringBinary(irs);
                    Parameter parameter_irs = new Parameter(parameterTypes + binary_irs.getAsInt(), Parameter.ParameterType.register, this);
                    parameters.add(parameter_irs);

                    String irt = binary.getBinaryValue().substring(11, 16);
                    StringBinary binary_irt = new StringBinary(irt);
                    Parameter parameter_irt = new Parameter(parameterTypes + binary_irt.getAsInt(), Parameter.ParameterType.register, this);
                    parameters.add(parameter_irt);
                }


                String iimm = binary.getBinaryValue().substring(16, 32);
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
        switch (getInstructionOnly()) {
            case "ADD.S":
                return "ADDER";
            case "MUL.S":
                return "MULTIPLIER";
            default:
                return "INTEGER";
        }
    }

    public void addDependencies() {
        for (Parameter p : parameters) {
            p.addDependency();
        }
    }

    public void analyzeDependencies() {
        ArrayList<Parameter> parameters = getParameters();

        for (Parameter p : parameters) {
            if (p.getParameter() instanceof Register)
                p.analyzeDependency();
        }
    }

    public DataDependencyException getDependencyWithBlock() {
        DataDependencyException dataDependencyException = null;
        try {
            dataDependencyException = checkDependencies();
        } catch (DataDependencyException e) {
            dataDependencyException = e;
        }
        return dataDependencyException;
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

    public String getMemAddressHex() {
        return memAddressHex;
    }

    public Long getMemAddressLong() {
        return RadixHelper.convertHexToStringBinary(memAddressHex).getAsLong();
    }

    public Integer getMemAddressInt() {
        return RadixHelper.convertHexToStringBinary(memAddressHex).getAsInt();
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public int getCycle() {
        return cycle;
    }

    public DataDependencyException checkDependencies() {
        DataDependencyException dependency = getDependency();
        DataDependencyManager.DataHazardType hazardType = null;

        if (dependency != null) {
            Parameter parameter = dependency.getBecauseOfThisParameter();
            parameter.analyzeDependency();
            Instruction dependentOnThisInstruction = dependency.getDependentOnThis();
            Instruction blockedInstruction = dependency.getInstruction();

            Instruction.Stage releaseStage, blockStage;
            if (parameter.getParameterType() == Parameter.ParameterType.register) {


                if (parameter.getDependencyType() == Parameter.DependencyType.write) {
                    releaseStage = Instruction.Stage.WB;
                    blockStage = Instruction.Stage.MEM;
                    hazardType = DataDependencyManager.DataHazardType.WriteAfterWrite;
                } else {
                    releaseStage = Instruction.Stage.WB;
                    blockStage = Instruction.Stage.ID;
                    hazardType = DataDependencyManager.DataHazardType.ReadAfterWrite;
                }

            } else if (parameter.getParameterType() == Parameter.ParameterType.memory) {
                releaseStage = Instruction.Stage.MEM;
                blockStage = Instruction.Stage.MEM;

                if (parameter.getDependencyType() == Parameter.DependencyType.write)
                    hazardType = DataDependencyManager.DataHazardType.WriteAfterWrite;
                else
                    hazardType = DataDependencyManager.DataHazardType.ReadAfterWrite;
            } else {
                releaseStage = null;
                blockStage = null;
            }

            if ((releaseStage != null && blockStage != null)) {
                dependency.setDataDependencyBlock(new DataDependencyManager.DataDependencyBlock(dependentOnThisInstruction, releaseStage, blockStage, hazardType));

                if (parameter.getParameter() instanceof Register && stage == Stage.ID && hazardType == DataDependencyManager.DataHazardType.ReadAfterWrite) {
                    throw dependency;
                }

                if (parameter.getParameter() instanceof Register && stage == Stage.WB && hazardType == DataDependencyManager.DataHazardType.WriteAfterWrite)
                    throw dependency;
            }
        }
        return dependency;
    }

    public DataDependencyException getDependency() {
        ArrayList<Parameter> parameters = getParameters();

        // If I have dependencies, block
        for (Parameter param : parameters) {
            if (param.getParameter() instanceof Register) {
                param.analyzeDependency();
                Instruction dependentOnThis = param.peekDependency();
                if (dependentOnThis != null && !this.equals(dependentOnThis) && dependentOnThis.getStage() != Stage.DONE) {
                    return new DataDependencyException(this, dependentOnThis, param);
                }
            }
        }
        return null;
    }
}
