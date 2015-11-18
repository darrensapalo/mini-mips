package dlsu.advcarc.parser;

import dlsu.advcarc.register.Register;
import dlsu.advcarc.register.RegisterManager;
import junit.framework.TestCase;

/**
 * Created by Darren on 11/6/2015.
 */
public class InstructionCheckerTest extends TestCase {

    public static final String SAMPLE_INSTRUCTION_1 = "somelabel: DADDU R1, R2, R3";
    public static final String SAMPLE_INSTRUCTION_2 = "doThis: DADDU R1";
    public static final String SAMPLE_INSTRUCTION_3 = "try_to_work: BNE R1, R2, doThis";
    public static final String SAMPLE_INSTRUCTION_4 = "___Something_weird: J somelabel;";
    public static final String SAMPLE_INSTRUCTION_5 = "LW R1, 1000(R2);";
    public static final String SAMPLE_INSTRUCTION_6 = "SW R3, 0000(R0)";

    public void testValidateInstruction() throws Exception {

        boolean DADDU = InstructionChecker.validateInstruction("DADDU");
        assertTrue(DADDU);
        boolean DMULT = InstructionChecker.validateInstruction("DMULT");
        assertTrue(DMULT);
        boolean OR = InstructionChecker.validateInstruction("OR");
        assertTrue(OR);
        boolean SLT = InstructionChecker.validateInstruction("SLT");
        assertTrue(SLT);
        boolean BEQ = InstructionChecker.validateInstruction("BEQ");
        assertTrue(BEQ);
        boolean LW = InstructionChecker.validateInstruction("LW");
        assertTrue(LW);
        boolean LWU = InstructionChecker.validateInstruction("LWU");
        assertTrue(LWU);
        boolean SW = InstructionChecker.validateInstruction("SW");
        assertTrue(SW);
        boolean DSLL = InstructionChecker.validateInstruction("DSLL");
        assertTrue(DSLL);
        boolean ANDI = InstructionChecker.validateInstruction("ANDI");
        assertTrue(ANDI);
        boolean DADDIU = InstructionChecker.validateInstruction("DADDIU");
        assertTrue(DADDIU);
        boolean J = InstructionChecker.validateInstruction("J");
        assertTrue(J);
        boolean L = InstructionChecker.validateInstruction("L.S");
        assertTrue(L);
        boolean S = InstructionChecker.validateInstruction("S.S");
        assertTrue(S);
        boolean ADD = InstructionChecker.validateInstruction("ADD.S");
        assertTrue(ADD);
        boolean MUL = InstructionChecker.validateInstruction("MUL.S");
        assertTrue(MUL);


        boolean a = InstructionChecker.validateInstruction("WHISPER");
        assertFalse(a);
        boolean b = InstructionChecker.validateInstruction("helloworld");
        assertFalse(b);
        boolean c = InstructionChecker.validateInstruction("something");
        assertFalse(c);
        boolean d = InstructionChecker.validateInstruction("!@@#");
        assertFalse(d);
        boolean e = InstructionChecker.validateInstruction("1234234");
        assertFalse(e);
        boolean f = InstructionChecker.validateInstruction("010101001");
        assertFalse(f);
        boolean g = InstructionChecker.validateInstruction("what");
        assertFalse(g);
    }

    public void testGetLabel() throws Exception {


        String label = InstructionChecker.parseLabel(SAMPLE_INSTRUCTION_1);
        assertEquals("somelabel", label);

        label = InstructionChecker.parseLabel(SAMPLE_INSTRUCTION_2);
        assertEquals("doThis", label);

        label = InstructionChecker.parseLabel(SAMPLE_INSTRUCTION_3);
        assertEquals("try_to_work", label);

        label = InstructionChecker.parseLabel(SAMPLE_INSTRUCTION_4);
        assertEquals("___Something_weird", label);
    }

    public void testCheckLine() throws Exception {

    }

    public void testGetInstruction() throws Exception {
        String label = InstructionChecker.parseInstruction(SAMPLE_INSTRUCTION_1);
        assertEquals("DADDU", label);

        label = InstructionChecker.parseInstruction(SAMPLE_INSTRUCTION_2);
        assertEquals("DADDU", label);

        label = InstructionChecker.parseInstruction(SAMPLE_INSTRUCTION_3);
        assertEquals("BNE", label);

        label = InstructionChecker.parseInstruction(SAMPLE_INSTRUCTION_4);
        assertEquals("J", label);
    }

    public void testGetParameters() throws Exception {
        Instruction instruction = InstructionChecker.getInstruction(SAMPLE_INSTRUCTION_1);
        assertEquals("R1", instruction.getParameters().get(0).toString());
        assertEquals("R2", instruction.getParameters().get(1).toString());
        assertEquals("R3", instruction.getParameters().get(2).toString());

        instruction = InstructionChecker.getInstruction(SAMPLE_INSTRUCTION_5);
        assertEquals("LW", instruction.getInstruction());

        assertEquals("R1", instruction.getParameters().get(0).toString());
        assertEquals("1000", instruction.getParameters().get(1).toString());
        assertEquals("R2", instruction.getParameters().get(2).toString());

        instruction = InstructionChecker.getInstruction(SAMPLE_INSTRUCTION_6);
        assertEquals("SW", instruction.getInstruction());

        assertEquals("R3", instruction.getParameters().get(0).toString());
        assertEquals("0000", instruction.getParameters().get(1).toString());
        assertEquals("R0", instruction.getParameters().get(2).toString());


    }

    public void testInstruction() throws Exception {
        Instruction instruction = InstructionChecker.getInstruction(SAMPLE_INSTRUCTION_1);
        assertEquals("DADDU", instruction.getInstruction());

        instruction = InstructionChecker.getInstruction(SAMPLE_INSTRUCTION_2);
        assertEquals("DADDU", instruction.getInstruction());

        instruction = InstructionChecker.getInstruction(SAMPLE_INSTRUCTION_3);
        assertEquals("BNE", instruction.getInstruction());
    }

    public void testRegistersSingleton() throws Exception {
        Instruction instruction = InstructionChecker.getInstruction(SAMPLE_INSTRUCTION_1);
        Instruction instruction2 = InstructionChecker.getInstruction(SAMPLE_INSTRUCTION_2);

        // References to parameters should be the same object; singleton pattern
        Parameter param1 = instruction.getParameters().get(0);
        Parameter param2 = instruction2.getParameters().get(0);

        assertEquals(param1, param2);
    }

    public void testDependencies() throws Exception {
        Memory.clear();
        RegisterManager.instance().clear();

        Instruction instruction = InstructionChecker.getInstruction(SAMPLE_INSTRUCTION_1);
        Instruction instruction2 = InstructionChecker.getInstruction(SAMPLE_INSTRUCTION_2);

        // References to parameters should be the same object; singleton pattern
        Parameter param1 = instruction.getParameters().get(0);
        param1.analyzeDependency();

        Parameter param2 = instruction2.getParameters().get(0);

        Instruction peekDependency = param2.peekDependency(Parameter.DependencyType.write);
        boolean equals = peekDependency.equals(instruction);

        assertTrue("That the instruction depends on the previous one, because they both write", equals);
    }
}