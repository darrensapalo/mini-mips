package dlsu.advcarc.cpurevised;

import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.server.Addresses;
import dlsu.advcarc.server.EventBusHolder;

/**
 * Created by user on 11/29/2015.
 */
public class ExecutionManager {

    /* Singleton */
    private static ExecutionManager instance;

    public static ExecutionManager instance() {
        if (instance == null)
            instance = new ExecutionManager();
        return instance;
    }

    /* Class */
    private ProgramCode programCode;
    private CPU cpu = new CPU();

    public void clockOnce() {
        cpu.clock();
    }

    public void clockFully() {
        while(cpu.clock());
    }

    public void reset(){
        this.programCode = null;
        this.cpu = new CPU();
    }

    public void inputProgramCode(ProgramCode programCode) {

        //Reset all
        this.programCode = programCode;
        MemoryManager.instance().inputProgramCode(programCode);
        this.cpu = new CPU();
        cpu.inputProgramCode(programCode);

        /* Broadcast the Updated Immediate Values */
        EventBusHolder.instance()
                .getEventBus()
                .publish(Addresses.CODE_BROADCAST,
                        programCode.toJsonObject()
                );
    }

    public ProgramCode getProgramCode(){
        return programCode;
    }

    public CPU getCPU(){
        return cpu;
    }

}
