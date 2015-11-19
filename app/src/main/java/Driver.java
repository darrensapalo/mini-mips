import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.cpu.ExecutionManager;
import dlsu.advcarc.parser.MipsParser;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.view.MiniMipsFrame;

import java.io.IOException;

/**
 * Created by Darren on 11/6/2015.
 */
public class Driver {


    public static void main(String[] args) throws Exception {

        ProgramCode programCode = MipsParser.parseFile("input.txt");

        // Initialize program counter
        programCode.setInitialProgramCounter(4000);


        // Set up cpu and input code
        ExecutionManager.instance().inputProgramCode(programCode);


        // Perform 5 clock cycles
        for (int i = 0; i < 5; i++) {
            ExecutionManager.instance().clockOnce();

            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // End
    }
}
