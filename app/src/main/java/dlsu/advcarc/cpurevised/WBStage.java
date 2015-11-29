package dlsu.advcarc.cpurevised;

import dlsu.advcarc.cpu.ALU;
import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.RegisterManager;
import io.vertx.core.json.JsonArray;

/**
 * Created by user on 11/29/2015.
 */
public class WBStage implements CPUStage{

    private Opcode IR;
    private StringBinary ALUOutput;

    private CPU cpu;
    private boolean hasInstructionToForward;

    @Override
    public boolean hasInstructionToForward() {
        return false;
    }

    @Override
    public void execute() {
        String destinationRegister = IR.getDestinationRegisterName();

        if(destinationRegister != null)
            RegisterManager.instance().updateRegister(destinationRegister, ALUOutput);
    }

    @Override
    public JsonArray toJsonArray() {
        return null;
    }

    @Override
    public void housekeeping(CPUStage previousStage) {
        MEMStage memStage = (MEMStage) previousStage;
        IR = memStage.getIR();
        ALUOutput = memStage.getALUOutput();
    }
}
