package dlsu.advcarc.cpu.stage.ex;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.cpu.block.DataDependencyManager;
import dlsu.advcarc.cpu.stage.InstructionDecodeStage;
import dlsu.advcarc.cpu.stage.InstructionFetchStage;
import dlsu.advcarc.cpu.stage.Stage;
import dlsu.advcarc.dependency.DataDependencyException;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by Darren on 11/24/2015.
 */
public class ExecuteStageSwitch extends Stage {
    private static final boolean DEBUG = false;
    private final InstructionDecodeStage instructionDecodeStage;

    private ExecuteStageInteger exInteger;
    private Instruction integer;
    private ExecuteStageAdder exAdder;
    private Instruction adder;
    private ExecuteStageMultiplier exMultiplier;
    private Instruction multiplier;

    private Memory EXMEM_IR;
    private String EXMEM_Cond;
    private Parameter EXMEM_B;
    private StringBinary EXMEM_ALUOutput;

    private LinkedList<Instruction> instructions;

    public ExecuteStageSwitch(CPU cpu, InstructionDecodeStage instructionDecodeStage, InstructionFetchStage fetch) {
        this.cpu = cpu;
        this.instructionDecodeStage = instructionDecodeStage;
        this.exInteger = new ExecuteStageInteger(this, cpu);
        this.exAdder = new ExecuteStageAdder(this, cpu);
        this.exMultiplier = new ExecuteStageMultiplier(this, cpu);
        this.instructions = new LinkedList<>();
        fetch.setExecuteStage(this);
        stageId = 2;
    }

    @Override
    public void housekeeping() {
        try {
            instruction = instructionDecodeStage.getInstruction();
            if (instruction == null)
                throw new Exception("Cannot run housekeeping on exStageSwitch because there is no instruction from ID yet.");

            if (instruction.getStage() != Instruction.Stage.EX)
                throw new Exception("Remains from the previous fetch; this instruction " + instruction + " has already been received by EX.");

            if (instructions.contains(instruction) == false)
                instructions.add(instruction);

            switch (instruction.getExecutionType()) {
                case "ADDER":
                    if (instruction.equals(exAdder.getInstruction()) == false) {
                        exAdder.housekeeping();
                        adder = instruction;
                    }
                    break;
                case "MULTIPLIER":
                    if (instruction.equals(exMultiplier.getInstruction()) == false) {
                        exMultiplier.housekeeping();
                        multiplier = instruction;
                    }
                    break;
                default:
                    if (instruction.equals(exInteger.getInstruction()) == false) {
                        exInteger.housekeeping();
                        integer = instruction;
                    }
                    break;
            }
        } catch (Exception e) {
            if (DEBUG)
                System.out.println(e.getMessage());
        }
    }

    @Override
    public void execute() {
        try {
            DataDependencyException dataDependencyException = integer.getDependencyWithBlock();

            if (dataDependencyException != null) {
                DataDependencyManager.DataDependencyBlock block = dataDependencyException.getDataDependencyBlock();
                if (block != null) {
                    Instruction ownedBy = block.getOwnedBy();
                    if (block.getDataHazardType() == DataDependencyManager.DataHazardType.WriteAfterWrite && dataDependencyException.getInstruction().getStage().ordinal() < block.getReleaseStage().ordinal())
                        System.out.println("Write after write can proceed!");
                    else if (ownedBy.getStage().ordinal() <= block.getReleaseStage().ordinal())
                        throw dataDependencyException;
                }
            }

            exInteger.execute();
        } catch (Exception e) {
            if (e.getMessage() != null) {
                e.getMessage();
            }
        }
        try {
            DataDependencyException dataDependencyException = adder.getDependencyWithBlock();

            if (dataDependencyException != null) {
                DataDependencyManager.DataDependencyBlock block = dataDependencyException.getDataDependencyBlock();
                if (block != null) {
                    Instruction ownedBy = block.getOwnedBy();
                    if (ownedBy.getStage().ordinal() < block.getReleaseStage().ordinal())
                        throw dataDependencyException;
                }
            }

            exAdder.execute();
        } catch (Exception e) {
            if (e.getMessage() != null) {
                e.getMessage();
            }
        }
        try {
            DataDependencyException dataDependencyException = multiplier.getDependencyWithBlock();

            if (dataDependencyException != null) {
                DataDependencyManager.DataDependencyBlock block = dataDependencyException.getDataDependencyBlock();
                if (block != null) {
                    Instruction ownedBy = block.getOwnedBy();
                    if (ownedBy.getStage().ordinal() < block.getReleaseStage().ordinal())
                        throw dataDependencyException;
                }
            }

            exMultiplier.execute();
        } catch (Exception e) {
            if (e.getMessage() != null) {
                e.getMessage();
            }
        }
    }

    @Override
    public void incrementInstruction() {
        incrementInstruction(adder, exAdder);
        incrementInstruction(integer, exInteger);
        incrementInstruction(multiplier, exMultiplier);
    }

    public void incrementInstruction(Instruction instruction, AbstractExecuteStage executeStage) {
        try {
            if (instruction == null)
                throw new Exception("Cannot increment instruction on " + executeStage.getClass().getSimpleName() + " because the execute stage does not have an instruction.");

            if (executeStage.didRun() == false)
                throw new Exception("Cannot increment instruction on " + executeStage.getClass().getSimpleName() + " because it did not run.");


            Instruction.Stage[] values = Instruction.Stage.values();

            executeStage.incrementInstruction();

            // Once the instruction reaches enough executions, move the instruction to the next stage which is MEM
            if (executeStage.currentNumberOfExecutionCycles == executeStage.numberOfExecutionCycles()) {
                instruction.setStage(values[stageId + 1]);

                Instruction.Stage stage = instruction.getStage();
                if (stage != null)
                    System.out.println(instruction + " was delivered to stage " + stage.toString());

                this.instruction = instruction;

                if (stage == Instruction.Stage.DONE)
                    this.instruction = null;


            }
        } catch (Exception ignored) {
            if (DEBUG)
                System.out.println(ignored.getMessage());
        }
    }

    public JsonArray toJsonArray() {
        return new JsonArray()
                .addAll(exInteger.toJsonArray())
                .addAll(exAdder.toJsonArray())
                .addAll(exMultiplier.toJsonArray());
    }

    public Memory getEXMEM_IR() {
        return EXMEM_IR;
    }

    public String getEXMEM_Cond() {
        return EXMEM_Cond;
    }

    public Parameter getEXMEM_B() {
        return EXMEM_B;
    }

    public StringBinary getEXMEM_ALUOutput() {
        return EXMEM_ALUOutput;
    }

    public void setEXMEM_IR(Memory EXMEM_IR) {
        this.EXMEM_IR = EXMEM_IR;
    }

    public void setEXMEM_Cond(String EXMEM_Cond) {
        this.EXMEM_Cond = EXMEM_Cond;
    }

    public void setEXMEM_B(Parameter EXMEM_B) {
        this.EXMEM_B = EXMEM_B;
    }

    public void setEXMEM_ALUOutput(StringBinary EXMEM_ALUOutput) {
        this.EXMEM_ALUOutput = EXMEM_ALUOutput;
    }

    public Memory getIDEX_IR() {
        return instructionDecodeStage.getIDEX_IR();
    }

    public Parameter getIDEX_B() {
        return instructionDecodeStage.getIDEX_B();
    }

    public Parameter getIDEX_A() {
        return instructionDecodeStage.getIDEX_A();
    }

    public Parameter getIDEX_IMM() {
        return instructionDecodeStage.getIDEX_IMM();
    }

    public StringBinary getIDEX_NPC() {
        return instructionDecodeStage.getIDEX_NPC();
    }

    public Instruction releaseInstruction() {
        Instruction peek = instructions.peekFirst();

        // Null if empty
        if (peek == null) return null;

        instruction = peek;

        // Pop if done
        if (peek.getStage() != Instruction.Stage.EX)
            return instructions.pop();

        // Peek only if not done
        return peek;
    }

    public boolean canStageRun(DataDependencyManager dataDependencyManager) throws Exception {
        didRun = false;
        if (this.getInstruction() == null)
            throw new Exception("NOP");

        Instruction instruction = this.getOldestInstruction();
        if (instruction != null && instruction.getStage().ordinal() != this.getStageId())
            throw new Exception("Cannot run " + this.getClass().getSimpleName() + " because the instruction of this stage has been passed, but is not yet at this stage.");

        return super.canStageRun(dataDependencyManager);
    }

    public Instruction getInstruction() {
        Instruction instruction = this.instruction;
        if (instruction == null || instruction.getStage() == Instruction.Stage.DONE)
            return null;

        return instruction;
    }

    private Instruction getOldestInstruction() {
        Instruction oldest = null;
        int oldestCycle = 9999;


        if (integer != null) {

            if (integer.getStage() == Instruction.Stage.DONE)
                integer = null;
            else if (integer.getCycle() < oldestCycle) {
                oldest = integer;
                oldestCycle = integer.getCycle();
            }
        }

        if (adder != null) {
            if (adder.getStage() == Instruction.Stage.DONE)
                adder = null;
            else if (adder.getCycle() < oldestCycle) {
                oldest = adder;
                oldestCycle = adder.getCycle();
            }
        }

        if (multiplier != null) {
            if (multiplier.getStage() == Instruction.Stage.DONE)
                multiplier = null;
            else if (multiplier.getCycle() < oldestCycle) {
                oldest = multiplier;
            }
        }
        return oldest;
    }

    public Instruction getIdInstruction() {
        return instructionDecodeStage.getInstruction();
    }

    public ExecuteStageInteger getExInteger() {
        return exInteger;
    }

    public ExecuteStageAdder getExAdder() {
        return exAdder;
    }

    public ExecuteStageMultiplier getExMultiplier() {
        return exMultiplier;
    }
}
