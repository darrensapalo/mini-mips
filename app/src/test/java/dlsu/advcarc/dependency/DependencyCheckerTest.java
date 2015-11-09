package dlsu.advcarc.dependency;

import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Darren on 11/10/2015.
 */
public class DependencyCheckerTest {


    public static final String SAMPLE_INSTRUCTION_1 = "somelabel: DADDU R1, R2, R3";
    public static final String SAMPLE_INSTRUCTION_2 = "doThis: DADDU R1";
    public static final String SAMPLE_INSTRUCTION_3 = "try_to_work: BEQ R1, R2, doThis";
    public static final String SAMPLE_INSTRUCTION_4 = "___Something_weird: J somelabel;";

    @Test
    public void testCheck() throws Exception {
        Instruction instruction = new Instruction(SAMPLE_INSTRUCTION_1);
        Instruction instruction2 = new Instruction(SAMPLE_INSTRUCTION_2);
        Instruction instruction3 = new Instruction(SAMPLE_INSTRUCTION_3);



        ArrayList<Parameter> parameters = instruction.getParameters();
        Parameter parameter = parameters.get(0);

        Parameter.DependencyType type = DependencyChecker.check(parameter.getParameter(), instruction);

        assertEquals("That the type is write dependency", Parameter.DependencyType.write, type);



        ArrayList<Parameter> parameters3 = instruction3.getParameters();
        Parameter parameter2 = parameters3.get(1);

        type = DependencyChecker.check(parameter2.getParameter(), instruction3);

        assertEquals("That the type is read dependency", Parameter.DependencyType.read, type);
    }
}