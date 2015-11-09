package dlsu.advcarc.parser;

import junit.framework.TestCase;

/**
 * Created by Darren on 11/6/2015.
 */
public class InstructionCheckerTest extends TestCase {


    public static final String SAMPLE_INSTRUCTION_1 = "somelabel: DADDU R1, R2, R3";
    public static final String SAMPLE_INSTRUCTION_2 = "doThis: DADDU R1";
    public static final String SAMPLE_INSTRUCTION_3 = "try_to_work: BNE R1, R2, doThis";
    public static final String SAMPLE_INSTRUCTION_4 = "___Something_weird: J somelabel;";

    private InstructionChecker instructionChecker;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instructionChecker = new InstructionChecker();

    }

    public void testValidateInstruction() throws Exception {

        boolean DADDU = instructionChecker.validateInstruction("DADDU");
        assertTrue(DADDU);
        boolean DMULT = instructionChecker.validateInstruction("DMULT");
        assertTrue(DMULT);
        boolean OR = instructionChecker.validateInstruction("OR");
        assertTrue(OR);
        boolean SLT = instructionChecker.validateInstruction("SLT");
        assertTrue(SLT);
        boolean BEQ = instructionChecker.validateInstruction("BEQ");
        assertTrue(BEQ);
        boolean LW = instructionChecker.validateInstruction("LW");
        assertTrue(LW);
        boolean LWU = instructionChecker.validateInstruction("LWU");
        assertTrue(LWU);
        boolean SW = instructionChecker.validateInstruction("SW");
        assertTrue(SW);
        boolean DSLL = instructionChecker.validateInstruction("DSLL");
        assertTrue(DSLL);
        boolean ANDI = instructionChecker.validateInstruction("ANDI");
        assertTrue(ANDI);
        boolean DADDIU = instructionChecker.validateInstruction("DADDIU");
        assertTrue(DADDIU);
        boolean J = instructionChecker.validateInstruction("J");
        assertTrue(J);
        boolean L = instructionChecker.validateInstruction("L.S");
        assertTrue(L);
        boolean S = instructionChecker.validateInstruction("S.S");
        assertTrue(S);
        boolean ADD = instructionChecker.validateInstruction("ADD.S");
        assertTrue(ADD);
        boolean MUL = instructionChecker.validateInstruction("MUL.S");
        assertTrue(MUL);


        boolean a = instructionChecker.validateInstruction("WHISPER");
        assertFalse(a);
        boolean b = instructionChecker.validateInstruction("helloworld");
        assertFalse(b);
        boolean c = instructionChecker.validateInstruction("something");
        assertFalse(c);
        boolean d = instructionChecker.validateInstruction("!@@#");
        assertFalse(d);
        boolean e = instructionChecker.validateInstruction("1234234");
        assertFalse(e);
        boolean f = instructionChecker.validateInstruction("010101001");
        assertFalse(f);
        boolean g = instructionChecker.validateInstruction("what");
        assertFalse(g);
    }

    public void testGetLabel() throws Exception {


        String label = instructionChecker.getLabel(SAMPLE_INSTRUCTION_1);
        assertEquals("somelabel", label);

        label = instructionChecker.getLabel(SAMPLE_INSTRUCTION_2);
        assertEquals("doThis", label);

        label = instructionChecker.getLabel(SAMPLE_INSTRUCTION_3);
        assertEquals("try_to_work", label);

        label = instructionChecker.getLabel(SAMPLE_INSTRUCTION_4);
        assertEquals("___Something_weird", label);
    }

    public void testCheckLine() throws Exception {

    }

    public void testGetInstruction() throws Exception {
        String label = instructionChecker.getInstruction(SAMPLE_INSTRUCTION_1);
        assertEquals("DADDU", label);

        label = instructionChecker.getInstruction(SAMPLE_INSTRUCTION_2);
        assertEquals("DADDU", label);

        label = instructionChecker.getInstruction(SAMPLE_INSTRUCTION_3);
        assertEquals("BNE", label);

        label = instructionChecker.getInstruction(SAMPLE_INSTRUCTION_4);
        assertEquals("J", label);
    }

    public void testGetParameters() throws Exception {
        String[] params = instructionChecker.getParameters(SAMPLE_INSTRUCTION_1);
        assertEquals("R1", params[0]);
        assertEquals("R2", params[1]);
        assertEquals("R3", params[2]);
    }
}