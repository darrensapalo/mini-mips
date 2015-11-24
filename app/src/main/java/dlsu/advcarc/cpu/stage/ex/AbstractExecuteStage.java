package dlsu.advcarc.cpu.stage.ex;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.cpu.stage.Stage;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;

/**
 * Created by Darren on 11/24/2015.
 */
public abstract class AbstractExecuteStage extends Stage {
    protected final CPU cpu;
    protected final ExecuteStageSwitch executeStageSwitch;

    protected Memory ir;
    protected String EXMEM_Cond;
    protected Parameter EXMEM_B;
    protected StringBinary EXMEM_ALUOutput;

    protected Parameter a;
    protected Parameter b;
    protected Parameter imm;
    protected StringBinary npc;
    protected Instruction instruction;

    protected int currentNumberOfExecutionCycles;


    public AbstractExecuteStage(CPU cpu, ExecuteStageSwitch executeStageSwitch){
        this.cpu = cpu;
        this.executeStageSwitch = executeStageSwitch;
        stageId = 2;
    }

    @Override
    public void execute() {
        executeStageSwitch.setEXMEM_ALUOutput(EXMEM_ALUOutput);
        executeStageSwitch.setEXMEM_Cond(EXMEM_Cond);
    }

    @Override
    public void housekeeping() {
        ir = executeStageSwitch.getIDEX_IR();
        executeStageSwitch.setEXMEM_IR(ir);

        EXMEM_B = executeStageSwitch.getIDEX_B();
        executeStageSwitch.setEXMEM_B(EXMEM_B);

        instruction = executeStageSwitch.getIdInstruction();
        currentNumberOfExecutionCycles = 0;
    }

    public Memory getEXMEM_IR() {
        return ir;
    }

    public void setIr(Memory ir) {
        this.ir = ir;
    }

    public String getEXMEM_Cond() {
        return EXMEM_Cond;
    }

    public void setEXMEM_Cond(String EXMEM_Cond) {
        this.EXMEM_Cond = EXMEM_Cond;
    }

    public Parameter getEXMEM_B() {
        return EXMEM_B;
    }

    public void setEXMEM_B(Parameter EXMEM_B) {
        this.EXMEM_B = EXMEM_B;
    }

    public StringBinary getEXMEM_ALUOutput() {
        return EXMEM_ALUOutput;
    }

    public void setEXMEM_ALUOutput(StringBinary EXMEM_ALUOutput) {
        this.EXMEM_ALUOutput = EXMEM_ALUOutput;
    }

    public Parameter getA() {
        return a;
    }

    public void setA(Parameter a) {
        this.a = a;
    }

    public Parameter getB() {
        return b;
    }

    public void setB(Parameter b) {
        this.b = b;
    }

    public Parameter getImm() {
        return imm;
    }

    public void setImm(Parameter imm) {
        this.imm = imm;
    }

    public StringBinary getNpc() {
        return npc;
    }

    public void setNpc(StringBinary npc) {
        this.npc = npc;
    }

    public void setInstruction(Instruction instruction){
        this.instruction = instruction;
    }

    public abstract int numberOfExecutionCycles();

    @Override
    public void incrementInstruction() {
        currentNumberOfExecutionCycles++;
    }
}
