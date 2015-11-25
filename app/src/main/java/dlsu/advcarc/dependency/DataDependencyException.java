package dlsu.advcarc.dependency;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.cpu.block.DataDependencyManager;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;

/**
 * Created by Darren on 11/25/2015.
 */
public class DataDependencyException extends IllegalStateException {

    private final Instruction instruction;
    private final Instruction dependentOnThis;
    private final Parameter becauseOfThisParameter;

    private DataDependencyManager.DataDependencyBlock dataDependencyBlock;

    public DataDependencyException(Instruction instruction, Instruction dependentOnThis, Parameter becauseOfThisParameter) {
        this.instruction = instruction;
        this.dependentOnThis = dependentOnThis;
        this.becauseOfThisParameter = becauseOfThisParameter;
    }

    @Override
    public String getMessage() {
        return "Cannot proceed because " + instruction.toString() + " has a write dependency on " + dependentOnThis.toString();
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public Instruction getDependentOnThis() {
        return dependentOnThis;
    }

    public Parameter getBecauseOfThisParameter() {
        return becauseOfThisParameter;
    }

    public void setDataDependencyBlock(DataDependencyManager.DataDependencyBlock dataDependencyBlock) {
        this.dataDependencyBlock = dataDependencyBlock;
    }

    public DataDependencyManager.DataDependencyBlock getDataDependencyBlock() {
        return dataDependencyBlock;
    }

    public void handle(CPU cpu) {
        DataDependencyManager.DataDependencyBlock block = getDataDependencyBlock();
        if (block != null) {
            cpu.addDataDependencyBlock(block);
        }
    }
}
