package dlsu.advcarc.cpurevised;

import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 11/30/2015.
 */
public class EXAdder extends AbstractEXStage {

    public static final int NUM_CYCLES_NEEDED = 4;

    private EXInstruction[] instructions = new EXInstruction[NUM_CYCLES_NEEDED];
    private EXInstruction toExecute;

    protected EXAdder(CPU cpu) {
        super(cpu);
    }

    @Override
    public boolean hasCompletedExeuction() {
        return instructions[instructions.length-1] != null;
    }

    @Override
    public boolean hasInstructionToForward() {
        return false; //unused
    }

    @Override
    protected boolean checkExtraDependenciesIfCanExecute() {
        return true;
    }

    @Override
    protected void execute() {

        for(int i=instructions.length-1; i>0; i--){
                instructions[i] = instructions[i-1];
        }

        instructions[0] = toExecute;
        toExecute = null;

        if(instructions[instructions.length-1] != null){
            EXInstruction exInstruction = instructions[instructions.length-1];
            ALUOutput = StringBinary.valueOf(exInstruction.A.getAsFloat() + exInstruction.B.getAsFloat());
        }
    }

    @Override
    public void resetRegisters() {
        A = StringBinary.valueOf(0);
        B = StringBinary.valueOf(0);
        IMM = StringBinary.valueOf(0);
        ALUOutput = StringBinary.valueOf(0);
        cond = 0;
        NPC = StringBinary.valueOf(0);
    }

    @Override
    public JsonArray toJsonArray() {

        JsonArray jsonArray = new JsonArray();

        int i=1;
        for(EXInstruction instruction: instructions){
            if(instruction != null){
                jsonArray.addAll(instruction.toJsonArray(i));
            }
            i++;
        }

        jsonArray.add(new JsonObject().put("register", "Adder ALUOutput").put("value", ALUOutput.toHexString(16)));

//        System.out.println(jsonArray.toString());

        return jsonArray;

    }

    public EXInstruction dequeue(){
        EXInstruction last = instructions[instructions.length-1];
        instructions[instructions.length-1] = null;
        return last;
    }

    private boolean isArrayEmpty(){
        for(EXInstruction instruction: instructions)
            if(instruction != null)
                return false;

        return toExecute == null;
    }

    @Override
    public boolean isNOP(){

//        System.out.println(isArrayEmpty()+" and "+toExecute==null);

        return isArrayEmpty() && toExecute == null;
    }

    @Override
    public void housekeeping(AbstractStage previousStage) {
        IDStage idStage = (IDStage) previousStage;
//        A = idStage.getA();
//        B = idStage.getB();
//        IMM = idStage.getIMM();
//        NPC = idStage.getNPC();
        this.IR = idStage.getIR();
//        IRMemAddressHex = idStage.getIRMemAddressHex();

        EXInstruction exInstruction = new EXInstruction();
        exInstruction.A = idStage.getA();
        exInstruction.B = idStage.getB();
        exInstruction.IMM = idStage.getIMM();
        exInstruction.NPC = idStage.getNPC();
        exInstruction.IR = idStage.getIR();
        exInstruction.IRMemAddressHex = idStage.getIRMemAddressHex();

        toExecute =  exInstruction;
    }

    public boolean canAccommodateNewInstruction(){
        return true;
    }

    public List<String> getActiveInstructionsMem() {
        List<String> mem = new ArrayList<String>();
        for(EXInstruction instruction: instructions)
            if(instruction != null)
                mem.add(instruction.IRMemAddressHex);

        return mem;
    }

    public List<Opcode> getActiveInstructionsLastCycle() {
        List<Opcode> mem = new ArrayList<Opcode>();
        for(EXInstruction instruction: instructions)
            if(instruction != null)
                mem.add(instruction.IR);

        return mem;
    }


}
