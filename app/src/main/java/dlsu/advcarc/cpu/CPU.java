package dlsu.advcarc.cpu;

import dlsu.advcarc.cpu.stage.*;
import dlsu.advcarc.cpu.stage.Memory;
import dlsu.advcarc.memory.*;
import dlsu.advcarc.opcode.*;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;

/**
 * Created by Darren on 11/9/2015.
 */
public class CPU {
    InstructionFetch instructionFetchStage = null;
    InstructionDecode instructionDecodeStage = null;
    Execute executeStage = null;
    Memory memoryStage = null;
    WriteBack writeBackStage = null;

    private ProgramCode code;

    private int programCounter;

    public void input(ProgramCode code){
        this.code = code;
        programCounter = code.InitialProgramCounter();

        instructionFetchStage = new InstructionFetch(this, code);
        instructionDecodeStage = new InstructionDecode(this, instructionFetchStage);
        executeStage = new Execute(this, instructionDecodeStage, instructionFetchStage);
        memoryStage = new Memory(this, executeStage);
        writeBackStage = new WriteBack(this);
    }

    public void clock(){
        try {
            instructionFetchStage.execute();
            instructionDecodeStage.execute();
            executeStage.execute();
            memoryStage.execute();
            writeBackStage.execute();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            throw new IllegalStateException("Cannot run the CPU when the resources are not yet initialized. Please input a ProgramCode.");
        }
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public String executeCond(Instruction instruction, dlsu.advcarc.memory.Memory EXMEM_IR, String a, String b){
        StringBinary _a = new StringBinary(a);
        StringBinary _b = new StringBinary(b);

        String inst = instruction.getInstruction();
        switch (inst){
            case "SLT":
                return (_a.getAsInt() < _b.getAsInt()) ? "1" : "0";
        }
        return null;
    }

    public StringBinary executeALU(Instruction instruction, dlsu.advcarc.memory.Memory ir, String a, String b, String imm, StringBinary _npc) {
        StringBinary _a = new StringBinary(a);
        StringBinary _b = new StringBinary(b);
        StringBinary _imm = new StringBinary(imm);

        String inst = instruction.getInstruction();
        switch (inst){
            case "DADDU":
                return _a.plus(_b);

            case "OR":
                return _a.or(_b);

            case "SLT":
                return null;

            case "DMULT":
                return _a.times(_b);

            case "DSLL":
                String shiftAmount = ir.getAsBinary().substring(21, 25);
                StringBinary shiftAmountBinary = new StringBinary(shiftAmount);
                return _a.shiftRight(shiftAmountBinary.getAsInt());

            case "ADD.S":
                return _a.plus(_b);

            case "MUL.S":
                return _a.times(_b);

            case "LW":
            case "LWU":
            case "SW":
            case "L.S":
            case "S.S":
                // Load store instructions are 'memory references'
                // ALUOutput = A + Imm
                return _a.plus(_imm);

            case "ANDI":
                return _a.and(_imm);

            case "DADDIU":
                return _a.plus(_imm);

            case "J":
                return _imm.shiftRight(-2);

            case "BEQ":
                return _npc.plus(_imm.shiftRight(-2));
        }
        return null;
    }
}
