package dlsu.advcarc.cpurevised;

import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/29/2015.
 */
public class MEMStage implements CPUStage {

    private Opcode IR;
    private StringBinary LMD;
    private StringBinary ALUOutput;
    private StringBinary B;

    private CPU cpu;
    private boolean hasInstructionToForward;

    public MEMStage(CPU cpu){
        this.cpu = cpu;
        IR = Opcode.createNOP();
        LMD = StringBinary.valueOf(0);
        ALUOutput = StringBinary.valueOf(0);
        B = StringBinary.valueOf(0);
    }

    @Override
    public boolean hasInstructionToForward() {
        return false;
    }

    @Override
    public void execute() {

        switch(IR.getInstruction()){

            case "L.S":
            case "LW":
            case "LWU":
                String memAddressHex = ALUOutput.toHexString(4);
                LMD = MemoryManager.instance().getInstance(memAddressHex).read();
                break;

            case "S.S":
            case "SW":
                memAddressHex = ALUOutput.toHexString(4);
                LMD = StringBinary.valueOf(0);
                MemoryManager.instance().updateMemory(memAddressHex, B);
                break;
        }

    }

    @Override
    public JsonArray toJsonArray() {
        return new JsonArray()
                .add(new JsonObject().put("register", "MEM/WB.LMD").put("value", LMD.toHexString(16)))
                .add(new JsonObject().put("register", "MEM/WB.ALUOutput").put("value", ALUOutput.toHexString(16)))
                .add(new JsonObject().put("register", "MEM/WB.IR").put("value", IR.toHexString(16)));
    }

    @Override
    public void housekeeping(CPUStage previousStage) {
        EXSwitch exStage = (EXSwitch) previousStage;
        IR = exStage.getIR();
        ALUOutput = exStage.getALUOutput();
        B = exStage.getB();
    }

    /* Getters */
    public Opcode getIR() {
        return IR;
    }

    public StringBinary getLMD() {
        return LMD;
    }

    public StringBinary getALUOutput() {
        return ALUOutput;
    }

    public StringBinary getB() {
        return B;
    }

    public CPU getCpu() {
        return cpu;
    }
}
