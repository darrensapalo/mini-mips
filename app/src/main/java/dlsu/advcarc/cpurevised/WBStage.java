package dlsu.advcarc.cpurevised;

import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.RegisterManager;
import io.vertx.core.json.JsonArray;

/**
 * Created by user on 11/29/2015.
 */
public class WBStage extends AbstractStage{

    private StringBinary ALUOutput;
    private StringBinary LMD;

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

        if(destinationRegister != null && !destinationRegister.trim().isEmpty()) {
            switch(IR.getInstruction()){
                case "LW":
                case "LWU":
                case "L.S":
                    RegisterManager.instance().updateRegister(destinationRegister, LMD);
                    break;
                default:
                    RegisterManager.instance().updateRegister(destinationRegister, ALUOutput);
            }
        }
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
        LMD = memStage.getLMD();
    }

    @Override
    public void resetRegisters() {
        ALUOutput = StringBinary.valueOf(0);
    }

}
