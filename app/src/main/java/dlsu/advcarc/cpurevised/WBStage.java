package dlsu.advcarc.cpurevised;

import dlsu.advcarc.cpu.ALU;
import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.RegisterManager;
import io.vertx.core.json.JsonArray;

/**
 * Created by user on 11/29/2015.
 */
public class WBStage extends AbstractStage{

    private StringBinary ALUOutput;

    @Override
    public boolean hasInstructionToForward() {
        return true;
    }

    @Override
    public boolean execute() {

        if(IR == null || "NOP".equals(IR.getInstruction()))
            return false;

        String destinationRegister = IR.getDestinationRegisterName();

        if(destinationRegister != null)
            RegisterManager.instance().updateRegister(destinationRegister, ALUOutput);

        return true;
    }

    @Override
    public JsonArray toJsonArray() {
        return null;
    }

    @Override
    public void housekeeping(AbstractStage previousStage) {
        MEMStage memStage = (MEMStage) previousStage;
        IR = memStage.getIR();
        ALUOutput = memStage.getALUOutput();
        IRMemAddressHex = memStage.getIRMemAddressHex();
    }

    @Override
    public void resetRegisters() {
        ALUOutput = StringBinary.valueOf(0);
    }

    public String getIRMemAddressHex() {
        return IRMemAddressHex;
    }
}
