package dlsu.advcarc.cpurevised;

import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/29/2015.
 */
public class MEMStage extends AbstractStage {

    private StringBinary LMD;
    private StringBinary ALUOutput;
    private StringBinary B;

    public MEMStage(CPU cpu){
        super(cpu);
    }

    @Override
    public boolean hasInstructionToForward() {
        return !IR.isNOP();
    }

    @Override
    public boolean hasPendingWrite(String registerName) {
        return registerName.equals(IR.getDestinationRegisterName());
    }

    @Override
    protected boolean checkExtraDependenciesIfCanExecute() {
        return true;
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
                .add(new JsonObject().put("register", "MEM/WB.IR").put("value", getIRString()));
    }

    @Override
    public void housekeeping(AbstractStage previousStage) {
        AbstractEXStage exStage = (AbstractEXStage) previousStage;

        if(exStage instanceof EXIntegerStage){
            if("L.S".equals(exStage.getIR().getInstruction())) {
                if(cpu.checkWriteAfterWrite(exStage.getIR().getDestinationRegisterName()))
                    return;
            }
        }

        IR = exStage.getIR();
        ALUOutput = exStage.getALUOutput();
        B = exStage.getB();
        IRMemAddressHex = exStage.getIRMemAddressHex();
        exStage.resetToNOP();
    }

    @Override
    public void resetRegisters() {
        LMD = StringBinary.valueOf(0);
        ALUOutput = StringBinary.valueOf(0);
        B = StringBinary.valueOf(0);
    }

    /* Getters */

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
