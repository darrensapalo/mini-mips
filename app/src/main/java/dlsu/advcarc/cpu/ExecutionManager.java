package dlsu.advcarc.cpu;

import dlsu.advcarc.parser.ProgramCode;

/**
 * Created by user on 11/19/2015.
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

    private ExecutionManager(){}

    public void clockOnce() {
        cpu.clock();
    }

    public void clockFully() {
        for (int i = 0; i < 10; i++)
            cpu.clock(); //TODO keep on clocking until program finishes.
    }

    public void inputProgramCode(ProgramCode programCode) {
        this.programCode = programCode;
        cpu.input(programCode);
    }

    public ProgramCode getProgramCode(){
        return programCode;
    }


}
