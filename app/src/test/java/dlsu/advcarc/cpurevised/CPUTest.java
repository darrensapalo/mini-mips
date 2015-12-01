package dlsu.advcarc.cpurevised;

import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.parser.MipsParser;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.Register;
import dlsu.advcarc.register.RegisterManager;
import dlsu.advcarc.server.MipsVerticle;
import io.vertx.core.Vertx;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.BindException;

import static org.junit.Assert.*;

/**
 * Created by Darren on 12/1/2015.
 */
public class CPUTest {

    private Vertx vertx;
    private MipsVerticle verticle;
    private ExecutionManager em;

    @Before
    public void setUp() {
        vertx = Vertx.factory.vertx();
        verticle = new MipsVerticle();
        vertx.deployVerticle(verticle);

        RegisterManager.instance().clear();
        MemoryManager.instance().clear();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        em = ExecutionManager.instance();
    }

    @After
    public void tearDown() throws Exception {
        verticle.stop();
        em = null;
    }

    @Test
    public void testDADDU() throws Exception {
        String code = "DADDU R1,R2,R3";

        RegisterManager.instance().updateRegister("R2", StringBinary.valueOf(2));
        RegisterManager.instance().updateRegister("R3", StringBinary.valueOf(3));

        ProgramCode programCode = MipsParser.parseCodeString(code);

        em.inputProgramCode(programCode);

        em.clockOnce();
        assertEquals("Should be DADDU operation", "DADDU", cpu().getIfStage().getIR().getInstruction());
        em.clockOnce();
        em.clockOnce();
        em.clockOnce();
        em.clockOnce();

        Register r1 = RegisterManager.instance().getInstance("R1");

        assertEquals("2 + 3 should be 5", StringBinary.valueOf(5).getAsInt(), r1.getValue().getAsInt());
    }

    private CPU cpu() {
        return ExecutionManager.instance().getCPU();
    }

    @Test
    public void testDMULT() throws Exception {
        String code = "DMULT R1,R2";
        RegisterManager.instance().updateRegister("R1", StringBinary.valueOf(2));
        RegisterManager.instance().updateRegister("R2", StringBinary.valueOf(3));

        ProgramCode programCode = MipsParser.parseCodeString(code);

        em.inputProgramCode(programCode);

        em.clockOnce();
        assertEquals("Should be DMULT operation", "DMULT", cpu().getIfStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();
        em.clockOnce();
        em.clockOnce();

        assertEquals("2 * 3 should be 6", StringBinary.valueOf(6).getAsInt(), cpu().getLO().getAsInt());
    }

    @Test
    public void testDMULTnegative() throws Exception {
        String code = "DMULT R1,R2";
        RegisterManager.instance().updateRegister("R1", StringBinary.valueOf(-5));
        RegisterManager.instance().updateRegister("R2", StringBinary.valueOf(-4));

        ProgramCode programCode = MipsParser.parseCodeString(code);

        em.inputProgramCode(programCode);

        em.clockOnce();
        assertEquals("Should be DMULT operation", "DMULT", cpu().getIfStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();
        em.clockOnce();
        em.clockOnce();

        assertEquals("-5 * -4 should be 20", StringBinary.valueOf(20).getAsInt(), cpu().getLO().getAsInt());
    }

    @Test
    public void testDMULTLarge() throws Exception {
        String code = "DMULT R1,R2";

        // ?9223372036854775807?
        StringBinary _7fff_ffff_ffff_ffff = new StringBinary("0111111111111111111111111111111111111111111111111111111111111111");
        RegisterManager.instance().updateRegister("R1", _7fff_ffff_ffff_ffff);

        // 4
        RegisterManager.instance().updateRegister("R2", StringBinary.valueOf(4));

        ProgramCode programCode = MipsParser.parseCodeString(code);

        em.inputProgramCode(programCode);

        em.clockOnce();
        assertEquals("Should be DMULT operation", "DMULT", cpu().getIfStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        assertEquals("Should be executing in exInteger stage", "DMULT", cpu().getEXIntegerStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        // -4, wraps around
        StringBinary lo = new StringBinary("1111111111111111111111111111111111111111111111111111111111111100");
        lo.forceLength(64);

        StringBinary hi = new StringBinary("1111111111111111111111111111111111111111111111111111111111111111");
        hi.forceLength(64);

        assertEquals("9223372036854775807 * 4 should be -4", lo.forceLength(64), cpu().getLO().getBinaryValue());
        assertEquals("9223372036854775807 * 4 should be -4", hi.forceLength(64), cpu().getHI().getBinaryValue());
    }

    @Test
    public void testOR() throws Exception {
        String code = "OR R3,R1,R2";

        RegisterManager.instance().updateRegister("R1", StringBinary.valueOf(16));
        RegisterManager.instance().updateRegister("R2", StringBinary.valueOf(8));

        ProgramCode programCode = MipsParser.parseCodeString(code);

        em.inputProgramCode(programCode);

        em.clockOnce();
        assertEquals("Should be OR operation", "OR", cpu().getIfStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        assertEquals("Should be executing in exInteger stage", "OR", cpu().getEXIntegerStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        // -4, wraps around

        assertEquals("16 | 8 should give 24", StringBinary.valueOf(24).getAsInt(), RegisterManager.instance().getInstance("R3").read().getAsInt());

        RegisterManager.instance().updateRegister("R1", StringBinary.valueOf(8));
        RegisterManager.instance().updateRegister("R2", StringBinary.valueOf(8));

        programCode = MipsParser.parseCodeString(code);

        em.inputProgramCode(programCode);

        em.clockOnce();
        assertEquals("Should be OR operation", "OR", cpu().getIfStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        assertEquals("Should be executing in exInteger stage", "OR", cpu().getEXIntegerStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        // -4, wraps around

        assertEquals("8 | 8 should give 8", StringBinary.valueOf(8).getAsInt(), RegisterManager.instance().getInstance("R3").read().getAsInt());
    }

    @Test
    public void testSLT() throws Exception {

        // Case 1 - true

        String code = "SLT R1,R2,R3";
        RegisterManager.instance().updateRegister("R2", StringBinary.valueOf(8));
        RegisterManager.instance().updateRegister("R3", StringBinary.valueOf(16));

        ProgramCode programCode = MipsParser.parseCodeString(code);

        em.inputProgramCode(programCode);

        em.clockOnce();
        assertEquals("Should be SLT operation", "SLT", cpu().getIfStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        assertEquals("Should be executing in exInteger stage", "SLT", cpu().getEXIntegerStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        // -4, wraps around

        assertEquals("R1 should be 1, because 8 < 16", StringBinary.valueOf(1).getAsInt(), RegisterManager.instance().getInstance("R1").read().getAsInt());


        // Case 2 - equal values


        RegisterManager.instance().updateRegister("R2", StringBinary.valueOf(16));
        RegisterManager.instance().updateRegister("R3", StringBinary.valueOf(16));

        programCode = MipsParser.parseCodeString(code);

        em.inputProgramCode(programCode);

        em.clockOnce();
        assertEquals("Should be SLT operation", "SLT", cpu().getIfStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        assertEquals("Should be executing in exInteger stage", "SLT", cpu().getEXIntegerStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        // -4, wraps around

        assertEquals("R1 should be 0, because 16 < 16 == false", StringBinary.valueOf(0).getAsInt(), RegisterManager.instance().getInstance("R1").read().getAsInt());


        // Case 3 - greater than

        RegisterManager.instance().updateRegister("R2", StringBinary.valueOf(17));
        RegisterManager.instance().updateRegister("R3", StringBinary.valueOf(16));

        programCode = MipsParser.parseCodeString(code);

        em.inputProgramCode(programCode);

        em.clockOnce();
        assertEquals("Should be SLT operation", "SLT", cpu().getIfStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        assertEquals("Should be executing in exInteger stage", "SLT", cpu().getEXIntegerStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        // -4, wraps around

        assertEquals("R1 should be 0, because 17 < 16 == false", StringBinary.valueOf(0).getAsInt(), RegisterManager.instance().getInstance("R1").read().getAsInt());
    }

    @Test
    public void testBEQfail() throws Exception {
        // Case 1 - true

        String code = "BEQ R1,R2,L1\r\n" +
                "DADDU R0, R0, R0\r\n" +
                "DADDU R0, R0, R0\r\n" +
                "DADDU R0, R0, R0\r\n" +
                "DADDU R0, R0, R0\r\n" +
                "DADDU R0, R0, R0\r\n" +
                "L1: DADDU R1, R2, R3";

        RegisterManager.instance().updateRegister("R1", StringBinary.valueOf(8));
        RegisterManager.instance().updateRegister("R2", StringBinary.valueOf(16));

        ProgramCode programCode = MipsParser.parseCodeString(code);

        em.inputProgramCode(programCode);

        em.clockOnce();
        assertEquals("Should be BEQ operation", "BEQ", cpu().getIfStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        assertEquals("Should be executing in exInteger stage", "BEQ", cpu().getEXIntegerStage().getIR().getInstruction());

        em.clockOnce();

        assertEquals("Cond should be 0 because 8 != 16", 0, cpu().getEXIntegerStage().getCond());
        em.clockOnce();

        // -4, wraps around

        assertEquals("NPC should return to 8, and not 24, because the branch did not continue [4, 8, 12, 16, 20]", StringBinary.valueOf(8).getAsInt(), cpu().getIfStage().getNPC().getAsInt());


    }

    @Test
    public void testBEQpass() throws Exception {

        // Case 2, branches

        String code = "BEQ R1,R2,L1\r\n" +
                "DADDU R0, R0, R0\r\n" +
                "DADDU R0, R0, R0\r\n" +
                "DADDU R0, R0, R0\r\n" +
                "DADDU R0, R0, R0\r\n" +
                "DADDU R0, R0, R0\r\n" +
                "L1: DADDU R1, R2, R3";

        RegisterManager.instance().updateRegister("R1", StringBinary.valueOf(8));
        RegisterManager.instance().updateRegister("R2", StringBinary.valueOf(8));

        ProgramCode programCode = MipsParser.parseCodeString(code);

        em.inputProgramCode(programCode);

        em.clockOnce();
        assertEquals("Should be BEQ operation", "BEQ", cpu().getIfStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        assertEquals("Should be executing in exInteger stage", "BEQ", cpu().getEXIntegerStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        // -4, wraps around

        assertEquals("NPC should be 24, because the branch was successful [4, 8, 12, 16, 20, L1 24]", StringBinary.valueOf(28).getAsInt(), cpu().getIfStage().getNPC().getAsInt());


    }

    @Test
    public void testLW() throws Exception {
        String code = "LW R1, 1000(R0)";
        ProgramCode programCode = MipsParser.parseCodeString(code);
        em.inputProgramCode(programCode);

        RegisterManager.instance().updateRegister("R1", StringBinary.valueOf(12));
        MemoryManager.instance().updateMemory("1000", StringBinary.valueOf(9));

        em.clockOnce();
        assertEquals("Should be LW operation", "LW", cpu().getIfStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        assertEquals("Should be executing in exInteger stage", "LW", cpu().getEXIntegerStage().getIR().getInstruction());

        em.clockOnce();

        assertEquals("LMD should be equal to 9", StringBinary.valueOf(9).getAsLong(), cpu().getMemStage().getLMD().getAsLong());

        em.clockOnce();

        // -4, wraps around

        assertEquals("R1 should be 9 because the load was successful", StringBinary.valueOf(9).getAsLong(), RegisterManager.instance().getInstance("R1").read().getAsLong());
    }

    @Test
    public void testLWofSigned() throws Exception {
        String code = "LW R1, 1000(R0)";
        ProgramCode programCode = MipsParser.parseCodeString(code);
        em.inputProgramCode(programCode);

        RegisterManager.instance().updateRegister("R1", StringBinary.valueOf(12));
        // 32 bits of 0s, then 32 bits of 1s. This should be sign extended at EX stage
        MemoryManager.instance().updateMemory("1000", new StringBinary("11111111111111111111111111111111"));

        em.clockOnce();
        assertEquals("Should be LW operation", "LW", cpu().getIfStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        assertEquals("Should be executing in exInteger stage", "LW", cpu().getEXIntegerStage().getIR().getInstruction());

        em.clockOnce();

        StringBinary allOnes = new StringBinary("1111111111111111111111111111111111111111111111111111111111111111");
        assertEquals("LMD should be equal to FFFFFFFFFFFFFFFF", allOnes.getBinaryValue(), cpu().getMemStage().getLMD().getBinaryValue());

        em.clockOnce();

        // -4, wraps around

        assertEquals("R1 should be all ones because the load was successful", StringBinary.valueOf(-1).getAsLong(), RegisterManager.instance().getInstance("R1").read().getAsLong());
    }

    @Test
    public void testLWU() throws Exception {

        // how is this different from lw?
        String code = "LWU R1, 1000(R0)";
        ProgramCode programCode = MipsParser.parseCodeString(code);
        em.inputProgramCode(programCode);

        RegisterManager.instance().updateRegister("R1", StringBinary.valueOf(12));
        MemoryManager.instance().updateMemory("1000", new StringBinary("11111111111111111111111111111111"));

        em.clockOnce();
        assertEquals("Should be LWU operation", "LWU", cpu().getIfStage().getIR().getInstruction());

        em.clockOnce();
        em.clockOnce();

        assertEquals("Should be executing in exInteger stage", "LWU", cpu().getEXIntegerStage().getIR().getInstruction());

        em.clockOnce();

        StringBinary allOnes = new StringBinary("0000000000000000000000000000000011111111111111111111111111111111");
        assertEquals("LMD should be equal to 00000000FFFFFFFF", allOnes.getBinaryValue(), cpu().getMemStage().getLMD().getBinaryValue());

        em.clockOnce();

        // -4, wraps around

        assertEquals("R1 should be all ones because the load was successful", allOnes.getAsLong(), RegisterManager.instance().getInstance("R1").read().getAsLong());
    }

    @Test
    public void testSW() throws Exception {

    }

    @Test
    public void testDSLL() throws Exception {

    }

    @Test
    public void testANDI() throws Exception {

    }

    @Test
    public void testDADDIU() throws Exception {

    }
}