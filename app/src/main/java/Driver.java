import dlsu.advcarc.cpu.CPU;
import dlsu.advcarc.cpu.ExecutionManager;
import dlsu.advcarc.parser.MipsParser;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.server.MipsVerticle;
import dlsu.advcarc.view.MiniMipsFrame;
import io.vertx.core.Vertx;

import java.io.IOException;

/**
 * Created by Darren on 11/6/2015.
 */
public class Driver {


    public static void main(String[] args) throws Exception {

        Vertx vertx = Vertx.factory.vertx();
        vertx.deployVerticle(new MipsVerticle());

        ProgramCode programCode = MipsParser.parseFile("input.txt");

        // Set up cpu and input code
        ExecutionManager.instance().inputProgramCode(programCode);


        // Perform 5 clock cycles
        for (int i = 0; i < 5; i++) {
            ExecutionManager.instance().clockOnce();
        }

        // End
    }
}
