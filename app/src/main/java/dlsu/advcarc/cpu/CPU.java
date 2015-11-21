package dlsu.advcarc.cpu;

import dlsu.advcarc.cpu.stage.*;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.utils.RadixHelper;
import dlsu.advcarc.server.Addresses;
import dlsu.advcarc.server.EventBusHolder;
import io.vertx.core.json.JsonObject;

/**
 * Created by Darren on 11/9/2015.
 */
public class CPU {
    InstructionFetchStage instructionFetchStage = null;
    InstructionDecodeStage instructionDecodeStage = null;
    ExecuteStage executeStage = null;
    MemoryStage memoryStage = null;
    WriteBackStage writeBackStage = null;

    private ProgramCode code;

    private int dataDependencyBlock = -1;

    private StringBinary programCounter;

    public void input(ProgramCode code) {
        this.code = code;
        programCounter = RadixHelper.convertLongToStringBinary(code.InitialProgramCounter());
        code.writeToMemory();

        instructionFetchStage = new InstructionFetchStage(this, code);
        instructionDecodeStage = new InstructionDecodeStage(this, instructionFetchStage);
        executeStage = new ExecuteStage(instructionDecodeStage, instructionFetchStage);
        memoryStage = new MemoryStage(this, executeStage);
        writeBackStage = new WriteBackStage(this, memoryStage);
    }

    public void clock() {

        try {
            if (dataDependencyBlock <= 0)
                instructionFetchStage.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (dataDependencyBlock <= 1)
                instructionDecodeStage.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (dataDependencyBlock <= 2)
                executeStage.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (dataDependencyBlock <= 3)
                memoryStage.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (dataDependencyBlock <= 4)
                writeBackStage.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


        writeBackStage.housekeeping();

        memoryStage.housekeeping();
        executeStage.housekeeping();

        instructionDecodeStage.housekeeping();
        instructionFetchStage.housekeeping();

        EventBusHolder.instance().getEventBus()
                .publish(Addresses.CPU_BROADCAST, this.toJsonObject());

    }

    public StringBinary getProgramCounter() {
        return programCounter;
    }

    public JsonObject toJsonObject(){
        return new JsonObject()
                .put("registers", getRegistersJsonObject());
    }


    public JsonObject getRegistersJsonObject(){
        return new JsonObject()
                .put("IF/ID.IR", instructionFetchStage.getIFID_IR())
                .put("IF/ID.NPC", instructionFetchStage.getIFID_NPC())
                .put("ID/EX.A", instructionDecodeStage.getIDEX_A())
                .put("ID/EX.B", instructionDecodeStage.getIDEX_B())
                .put("ID/EX.Imm", instructionDecodeStage.getIDEX_IMM())
                .put("ID/EX.IR", instructionDecodeStage.getIDEX_IR())
                .put("ID/EX.NPC", instructionDecodeStage.getIDEX_NPC())
                .put("EX/MEM.B", executeStage.getEXMEM_B())
                .put("EX/MEM.ALUOutput", executeStage.getEXMEM_ALUOutput())
                .put("EX/MEM.Cond", executeStage.getEXMEM_Cond())
                .put("EX/MEM.IR", executeStage.getEXMEM_IR())
                .put("MEM/WB.LMD", memoryStage.getMEMWB_LMD())
                .put("MEM/WB.ALUOutput", memoryStage.getMEMWB_ALUOutput())
                .put("MEM/WB.IR", memoryStage.getMEMWB_IR());
    }

    public void setProgramCounter(StringBinary programCounter) {
        this.programCounter = programCounter;
    }

    public int getDataDependencyBlock() {
        return dataDependencyBlock;
    }

    public void setDataDependencyBlock(int dataDependencyBlock) {
        this.dataDependencyBlock = dataDependencyBlock;
    }
}
