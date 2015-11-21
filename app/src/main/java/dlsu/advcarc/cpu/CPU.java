package dlsu.advcarc.cpu;

import dlsu.advcarc.cpu.stage.*;
import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;
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

    private int programCounter;

    public void input(ProgramCode code){
        this.code = code;
        programCounter = code.InitialProgramCounter();

        instructionFetchStage = new InstructionFetchStage(this, code);
        instructionDecodeStage = new InstructionDecodeStage(instructionFetchStage);
        executeStage = new ExecuteStage(instructionDecodeStage, instructionFetchStage);
        memoryStage = new MemoryStage(executeStage);
        writeBackStage = new WriteBackStage(memoryStage);
    }

    public void clock(){
        try {
            instructionFetchStage.execute();
            instructionDecodeStage.execute();
            executeStage.execute();
            memoryStage.execute();
            writeBackStage.execute();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            throw new IllegalStateException("Cannot run the CPU when the resources are not yet initialized. Please input a ProgramCode.");
        }

        /*  Broadcast CPU State */
        EventBusHolder.instance().getEventBus()
                .publish(Addresses.CPU_BROADCAST, this.toJsonObject());
    }

    public int getProgramCounter() {
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
}
