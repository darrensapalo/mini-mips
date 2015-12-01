package dlsu.advcarc.cpurevised;

import dlsu.advcarc.opcode.Opcode;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.json.JsonArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 11/29/2015.
 */
public class EXSwitch extends AbstractStage {

    private EXIntegerStage integer;
    private EXAdder adder;
    private EXMultiplier multiplier;

    private List<String> activeInstructionsMem;
    private List<Opcode> activeInstructionsLastCycle;

    public EXSwitch(CPU cpu) {
        super(cpu);
        integer = new EXIntegerStage(cpu);
        adder = new EXAdder(cpu);
        multiplier = new EXMultiplier(cpu);
        activeInstructionsMem = new ArrayList<String>();
        activeInstructionsLastCycle = new ArrayList<Opcode>();
    }

    @Override
    public boolean hasInstructionToForward() {
        return false; // unused
    }

    @Override
    protected boolean checkExtraDependenciesIfCanExecute() {
        return true; //unused
    }

    @Override
    public boolean executeIfAllowed(AbstractStage nextStage) {
        activeInstructionsLastCycle.clear();
        activeInstructionsMem.clear();
        boolean integerExecuted = integer.executeIfAllowed(nextStage);
        boolean adderExecuted = adder.executeIfAllowed(nextStage);
        boolean multiplierExecuted = multiplier.executeIfAllowed(nextStage);

        if (integerExecuted) {
            activeInstructionsLastCycle.add(integer.getIR());
            activeInstructionsMem.add(integer.getIRMemAddressHex());
        }
        if (adderExecuted) {
//            activeInstructionsLastCycle.addAll(adder.getActiveInstructionsLastCycle());
//            activeInstructionsMem.addAll(adder.getActiveInstructionsMem());
//            activeInstructionsLastCycle.add(adder.getIR());
//            activeInstructionsMem.add(adder.getIRMemAddressHex());
        }
        if (multiplierExecuted) {
            activeInstructionsLastCycle.add(multiplier.getIR());
            activeInstructionsMem.add(multiplier.getIRMemAddressHex());
        }

        return integerExecuted || adderExecuted || multiplierExecuted;
    }

    @Override
    public void execute() {
        //This method should not be called
    }

    @Override
    public JsonArray toJsonArray() {
        return new JsonArray().addAll(integer.toJsonArray())
                .addAll(adder.toJsonArray())
                .addAll(multiplier.toJsonArray());
    }

    @Override
    public void housekeeping(AbstractStage previousStage) {
        AbstractEXStage targetStage = getTargetStage(previousStage.getIR());
        targetStage.housekeeping(previousStage);
    }

    @Override
    public void resetRegisters() {
        //TODO check if we need to do anything here
//        if (integer != null)
//            integer.resetToNOP();
    }

    @Override
    public boolean isNOP() {
        return integer.isNOP() && adder.isNOP() && multiplier.isNOP();
    }

    public boolean hasStall() {
        return integer.hasStalledLS(); // it's impossible to stall in adder or multiplier || adder.isStalling() || multiplier.isStalling();
    }

    public boolean isTargetStageNOP(Opcode opcode) {
        AbstractEXStage targetStage = getTargetStage(opcode);
        return targetStage.isNOP();
    }

    public boolean canAccommodate(Opcode opcode){
        AbstractEXStage targetStage = getTargetStage(opcode);

        if(targetStage instanceof EXAdder || targetStage instanceof EXMultiplier)
            return true;

        return targetStage.isNOP();
    }

    public AbstractEXStage getStageToForward() {
        if (!multiplier.isNOP() && multiplier.hasCompletedExeuction())
            return multiplier;

        if (!adder.isNOP() && adder.hasCompletedExeuction())
            return adder;

        if (!integer.isNOP() && integer.hasCompletedExeuction() && !integer.isStalling())
            return integer;

        return null;
    }

    public AbstractEXStage getTargetStage(Opcode opcode) {
        switch (opcode.getInstruction()) {
            case "ADD.S":
                return adder;
            case "MUL.S":
                return multiplier;
            default:
                return integer;
        }
    }

    public List<String> getActiveInstructionsMem() {
        activeInstructionsMem.addAll(adder.getActiveInstructionsMem());
        return activeInstructionsMem;
    }

    public List<Opcode> getActiveInstructionsLastCycle() {
        activeInstructionsLastCycle.addAll(adder.getActiveInstructionsLastCycle());
        return activeInstructionsLastCycle;
    }

    @Override
    public boolean hasPendingWrite(String registerName) {
        return multiplier.hasPendingWrite(registerName) ||
                adder.hasPendingWrite(registerName) ||
                integer.hasPendingWrite(registerName);
    }

    public boolean hasPendingFloatingWrite(String registerName) {
        return multiplier.hasPendingWrite(registerName) ||
                adder.hasPendingWrite(registerName);
    }

    public EXIntegerStage getEXIntegerStage() {
        return integer;
    }

    public EXAdder getEXAdderStage() {
        return adder;
    }

    public EXMultiplier getEXMultiplierStage() {
        return multiplier;
    }
}
