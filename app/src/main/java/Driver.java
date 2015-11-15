import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.view.MiniMipsFrame;

/**
 * Created by Darren on 11/6/2015.
 */
public class Driver {


    public static void main(String[] args) {

        ProgramCode programCode = ProgramCode.readFile("input.txt");

        // Initialize program counter
        programCode.setInitialProgramCounter(4000);

        MiniMipsFrame miniMipsFrame = new MiniMipsFrame();

        // Set up cpu and input code
        CPU cpu = new CPU();
        cpu.input(programCode);

        // Perform 5 clock cycles
        for (int i = 0; i < 5; i++) {
            cpu.clock();

            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // End
    }
}
