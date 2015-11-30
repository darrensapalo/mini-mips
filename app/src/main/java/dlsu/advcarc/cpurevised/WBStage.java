package dlsu.advcarc.cpurevised;

import dlsu.advcarc.cpu.ALU;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.RegisterManager;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/29/2015.
 */
public class WBStage extends AbstractStage{

    private StringBinary ALUOutput;
    private StringBinary LMD;
    private String affectedRegister;
    private StringBinary affectedRegisterValue;

    public WBStage(CPU cpu){
        super(cpu);
        resetRegisters();
    }

    @Override
    public boolean hasInstructionToForward() {
        return true;
    }

    @Override
    protected boolean checkExtraDependenciesIfCanExecute() {
        return true;
    }

    @Override
    public void execute() {
        String destinationRegister = IR.getDestinationRegisterName();
        affectedRegister = destinationRegister;

        if(destinationRegister != null && !destinationRegister.trim().isEmpty()) {
            switch(IR.getInstruction()){
                case "LW":
                case "LWU":
                case "L.S":
                    RegisterManager.instance().updateRegister(destinationRegister, LMD);
                    affectedRegisterValue = LMD;
                    break;
                default:
                    RegisterManager.instance().updateRegister(destinationRegister, ALUOutput);
                    affectedRegisterValue = ALUOutput;
            }
        }
    }

    @Override
    public JsonArray toJsonArray() {
        return new JsonArray().add(
                new JsonObject().put("register", "Rn: ("+affectedRegister+")")
                .put("value", affectedRegisterValue.toHexString(16))
               );
    }

    @Override
    public void housekeeping(AbstractStage previousStage) {
        MEMStage memStage = (MEMStage) previousStage;
        IR = memStage.getIR();
        ALUOutput = memStage.getALUOutput();
        IRMemAddressHex = memStage.getIRMemAddressHex();
        LMD = memStage.getLMD();
    }

    @Override
    public void resetRegisters() {
        ALUOutput = StringBinary.valueOf(0);
        affectedRegisterValue = StringBinary.valueOf(0);
        affectedRegister = null;
    }

}
