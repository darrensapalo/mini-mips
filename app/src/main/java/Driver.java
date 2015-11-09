import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.parser.ProgramCode;

import java.io.File;
import java.util.Scanner;

/**
 * Created by Darren on 11/6/2015.
 */
public class Driver {


    public static void main(String[] args) {
        File file = new File("input.txt");
        ProgramCode programCode = new ProgramCode();

        // Read file
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                programCode.addInstruction(scanner.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize program counter
        programCode.setInitialProgramCounter(4000);


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
