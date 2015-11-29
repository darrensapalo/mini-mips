package dlsu.advcarc.cpurevised;

import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/29/2015.
 */
public class CPU {

    private IFStage ifStage;
    private IDStage idStage;
    private EXSwitch exStage;
    private MEMStage memStage;
    private WBStage wbStage;

    private ProgramCode programCode;

    public CPU(){
        reset();
    }

    private void reset(){
        ifStage = new IFStage(this);
        idStage = new IDStage();
        exStage = new EXSwitch();
        memStage = new MEMStage();
        wbStage = new WBStage();
        programCode = null;
    }

    public void inputProgramCode(ProgramCode programCode){
        reset();
        this.programCode = programCode;
    }

    public StringBinary getExMemAluOutput(){
        return new StringBinary("0"); //TODO
    }


    public boolean clock(){
        return true; //TODO
    }

    public JsonObject toJsonObject(){
        return new JsonObject(); //TODO
    }

}
