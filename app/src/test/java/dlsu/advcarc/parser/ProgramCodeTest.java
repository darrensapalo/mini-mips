package dlsu.advcarc.parser;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by Darren on 11/9/2015.
 */
public class ProgramCodeTest {


    public static final String SAMPLE_INSTRUCTION_1 = "somelabel: DADDU R1, R2, R3";
    public static final String SAMPLE_INSTRUCTION_2 = "doThis: DADDU R1";
    public static final String SAMPLE_INSTRUCTION_3 = "try_to_work: BNE R1, R2, doThis";
    public static final String SAMPLE_INSTRUCTION_4 = "___Something_weird: J somelabel;";

    @Test
    public void testAddInstruction() throws Exception {
        ProgramCode programCode = new ProgramCode();
        programCode.addInstruction(SAMPLE_INSTRUCTION_1);
        programCode.addInstruction(SAMPLE_INSTRUCTION_2);
        programCode.addInstruction(SAMPLE_INSTRUCTION_3);
        programCode.addInstruction(SAMPLE_INSTRUCTION_4);

        LinkedList<ProgramCode.Code> program = programCode.getProgram();
        ProgramCode.Code code = program.get(0);

        assertEquals(code.getLine(), SAMPLE_INSTRUCTION_1);
        assertEquals(code.getMemoryLocation(), 0);

        code = program.get(1);

        assertEquals(code.getLine(), SAMPLE_INSTRUCTION_2);
        assertEquals(code.getMemoryLocation(), 4);
    }
}