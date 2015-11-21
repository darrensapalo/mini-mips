package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.ALU;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.opcode.OpcodeHelper;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Darren on 11/9/2015.
 */
public class ExecuteStage extends Stage {
    private InstructionDecodeStage instructionDecodeStage;
    private Memory EXMEM_IR;
    private String EXMEM_Cond;
    private Parameter EXMEM_B;
    private StringBinary EXMEM_ALUOutput;
    private Parameter a;
    private Parameter b;
    private Parameter imm;
    private StringBinary npc;
    private Instruction instruction;

    public ExecuteStage(InstructionDecodeStage instructionDecodeStage, InstructionFetchStage instructionFetchStage) {
        this.instructionDecodeStage = instructionDecodeStage;
        instructionFetchStage.setExecuteStage(this);
    }

    @Override
    public void housekeeping() {
        EXMEM_IR = instructionDecodeStage.getIDEX_IR();
        EXMEM_B = instructionDecodeStage.getIDEX_B();

        instruction = instructionDecodeStage.getInstruction();
        a = instructionDecodeStage.getIDEX_A();
        b = instructionDecodeStage.getIDEX_B();
        imm = instructionDecodeStage.getIDEX_IMM();
        npc = instructionDecodeStage.getIDEX_NPC();

    }

    @Override
    public void execute() {
        String inst =  instruction.getInstruction();

        // depending on instruction, perform operation on a, b, or imm
        // EXMEM_ALUOutput =
        EXMEM_ALUOutput = ALU.executeALU(inst, EXMEM_IR, a, b, imm, npc);

        // depending on instruction, compute for cond
        EXMEM_Cond = ALU.executeCond(inst, EXMEM_IR, a, b);
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

    public Instruction getInstruction() {
        return instruction;
    }

    public JsonArray toJsonArray(){
        return new JsonArray()
                .add(new JsonObject().put("register", "EX/MEM.B").put("value", getEXMEM_B() == null? "null": getEXMEM_B().getValue()))
                .add(new JsonObject().put("register", "EX/MEM.ALUOutput").put("value", getEXMEM_ALUOutput() == null? "null": getEXMEM_ALUOutput().toHexString()))
                .add(new JsonObject().put("register", "EX/MEM.Cond").put("value", getEXMEM_Cond() == null? "null": getEXMEM_Cond()))
                .add(new JsonObject().put("register", "EX/MEM.IR").put("value", getEXMEM_IR() == null? "null": getEXMEM_IR().getAsHex()));

    }

}
