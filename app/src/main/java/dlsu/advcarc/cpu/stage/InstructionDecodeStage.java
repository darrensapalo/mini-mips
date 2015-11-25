package dlsu.advcarc.cpu.stage;

import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.immediate.register.Immediate;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Parameter;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;

/**
 * Created by Darren on 11/9/2015.
 */
public class InstructionDecodeStage extends Stage {
    private CPU cpu;
    private InstructionFetchStage instructionFetchStage;

    private Parameter IDEX_A;
    private Parameter IDEX_B;
    private Parameter IDEX_IMM;
    private Memory IDEX_IR;
    private StringBinary IDEX_NPC;
    private Immediate newImm;

    public InstructionDecodeStage(CPU cpu, InstructionFetchStage instructionFetchStage) {
        this.cpu = cpu;
        this.instructionFetchStage = instructionFetchStage;
        stageId = 1;
    }

    @Override
    public void housekeeping() {
        try {
            this.instruction = instructionFetchStage.getInstruction();
            if (instruction != null)
                System.out.println("ID Stage: Received a new instruction from IF stage - " + instruction);
            IDEX_IR = instructionFetchStage.getIFID_IR();
            IDEX_NPC = instructionFetchStage.getIFID_NPC();
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage());
        }
    }

    @Override
    public void execute() {
        // instructionFetchStage.get code
        didRun = false;

        instruction.analyzeDependencies();
        instruction.checkDependencies();
        instruction.addDependencies();

        ArrayList<Parameter> parameters = instruction.getParameters();

        try {
            IDEX_A = parameters.get(0);
        } catch (Exception e) {

        }

        try {
            IDEX_B = parameters.get(1);
        } catch (Exception e) {

        }

        try {
            IDEX_IMM = parameters.get(2);
        } catch (Exception e) {

        }
        didRun = true;
    }

    public Parameter getIDEX_A() {
        return IDEX_A;
    }

    public Parameter getIDEX_B() {
        return IDEX_B;
    }

    public Parameter getIDEX_IMM() {
        return IDEX_IMM;
    }

    public Memory getIDEX_IR() {
        return IDEX_IR;
    }

    public StringBinary getIDEX_NPC() {
        return IDEX_NPC;
    }

    public JsonArray toJsonArray() {
        return new JsonArray()
                .add(new JsonObject().put("register", "ID/EX.A").put("value", getIDEX_A() == null ? "null" : getIDEX_A().getValue()))
                .add(new JsonObject().put("register", "ID/EX.B").put("value", getIDEX_B() == null ? "null" : getIDEX_B().getValue()))
                .add(new JsonObject().put("register", "ID/EX.Imm").put("value", getIDEX_IMM() == null ? "null" : getIDEX_IMM().getValue()))
                .add(new JsonObject().put("register", "ID/EX.IR").put("value", getIDEX_IR() == null ? "null" : getIDEX_IR().getAsHex()))
                .add(new JsonObject().put("register", "ID/EX.NPC").put("value", getIDEX_NPC() == null ? "null" : getIDEX_NPC().toHexString()));
    }

}
