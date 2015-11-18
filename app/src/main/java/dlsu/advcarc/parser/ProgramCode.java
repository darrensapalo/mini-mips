package dlsu.advcarc.parser;

import dlsu.advcarc.cpu.stage.InstructionFetch;
import dlsu.advcarc.utils.RadixHelper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Darren on 11/9/2015.
 */
public class ProgramCode {
    public LinkedList<Code> code = new LinkedList<>();
    public LinkedList<Code> data = new LinkedList<>();

    private static int StartingMemoryAddress = 0;
    private static int StartingProgramAddress = 0;

    private Section currentSection;

    public ProgramCode() {
        currentSection = Section.Data;
    }

    public enum Section {
        Data, Program
    }

    public void addInstruction(String line) {
        line = line.trim();
        LinkedList<Code> container;

        if (line.equals(".data")) {
            currentSection = Section.Data;
            return;
        }

        if (line.equals(".text")) {
            currentSection = Section.Program;
            return;
        }

        container = ((currentSection == Section.Data) ? data : code);

        Code code = new Code(line, getNextAvailableAddress());
        container.add(code);
    }

    public LinkedList<Code> getProgram() {
        return code;
    }

    private int getNextAvailableAddress() {
        if (currentSection == Section.Data)
            return StartingMemoryAddress + data.size() * 4;
        return StartingProgramAddress + code.size() * 4;
    }

    public int InitialProgramCounter() {
        return StartingMemoryAddress;
    }

    public void setInitialProgramCounter(int start) {
        StartingProgramAddress = start;
    }

    public void setInitialMemoryCounter(int start) {
        StartingMemoryAddress = start;
    }

    public String getCode(int programCounter) {
        Iterator<Code> iterator = code.iterator();
        while (iterator.hasNext()) {
            Code next = iterator.next();
            if (next.getMemoryLocation() == programCounter)
                return next.getLine();
        }
        return null;
    }

    public void initialize() {
        compactify(data);
        initializeData();

        compactify(code);
    }

    public JsonArray toJsonArray(){
        JsonArray jsonArray = new JsonArray();
        for (Code codeEntry: code) {
            jsonArray.add(codeEntry.toJsonObject());
        }
        return jsonArray;
    }

    private void compactify(LinkedList<Code> codeList) {
        Code rememberPrevious = null;
        Iterator<Code> iterator = codeList.iterator();

        while (iterator.hasNext()) {
            Code current = iterator.next();

            if (current.getLine().isEmpty() && !current.getLabel().isEmpty()) {
                rememberPrevious = current;
                iterator.remove();
                continue;
            }

            if (rememberPrevious != null) {
                current.setLabel(rememberPrevious.getLabel());
                rememberPrevious = null;
            }
        }
    }

    private void initializeData() {
        Iterator<Code> iterator = data.iterator();
        while (iterator.hasNext()) {
            Code current = iterator.next();
            String[] split = current.getLine().split(" ");

            String memoryLocationHex = current.getMemoryLocationHex();
            Memory instance = Memory.getInstance(memoryLocationHex);
            String value = split[1];

            instance.write(value);
        }
        // use iterator, initialize memory modules
    }

    public class Code {
        private String label;
        private String line;
        private int memoryLocation;

        public Code(String line, int memoryLocation) {
            this.label = InstructionChecker.parseLabel(line);
            this.line = ((label == null) ? line : line.substring(this.label.length() + 1).trim());
            this.memoryLocation = memoryLocation;
        }

        public String getLine() {
            return line;
        }

        public int getMemoryLocation() {
            return memoryLocation;
        }

        public String getMemoryLocationHex() {
            return RadixHelper.padWithZero(Integer.toHexString(memoryLocation).toUpperCase(),4);
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return getMemoryLocationHex() + ": " + line;
        }

        public JsonObject toJsonObject(){
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("mem", getMemoryLocationHex());
            jsonObject.put("opcode", "00000000"); //TODO generate correct opcode
            jsonObject.put("instruction", line);
            return jsonObject;
        }

    }

    public static ProgramCode readFile(String filename) {
        ProgramCode programCode = new ProgramCode();

        // Read file
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {

                String line = scanner.nextLine();
                line = line.trim();

                // Ignore comments and whitespace
                if (line.startsWith(";")) continue;
                if (line.isEmpty()) continue;

                int i = line.indexOf(';');

                if (i != -1)
                    line = line.substring(0, i).trim();

                programCode.addInstruction(line);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not find input program at " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return programCode;
    }

    public static ProgramCode readCodeString(String codeString) {
        try {

            ProgramCode programCode = new ProgramCode();

            Scanner scanner = new Scanner(codeString);
            while (scanner.hasNext()) {

                String line = scanner.nextLine();
                line = line.trim();

                // Ignore comments and whitespace
                if (line.startsWith(";")) continue;
                if (line.isEmpty()) continue;

                int i = line.indexOf(';');

                if (i != -1)
                    line = line.substring(0, i).trim();

                programCode.addInstruction(line);
            }

            return programCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
