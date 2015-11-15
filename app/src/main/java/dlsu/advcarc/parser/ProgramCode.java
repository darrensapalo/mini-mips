package dlsu.advcarc.parser;

import dlsu.advcarc.cpu.stage.InstructionFetch;

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

        container = ((currentSection == Section.Data) ? code : data);

        Code code = new Code(line, getNextAvailableAddress());
        container.add(code);
    }

    public LinkedList<Code> getProgram() {
        return code;
    }

    private int getNextAvailableAddress() {
        if (currentSection == Section.Data)
            return StartingMemoryAddress + code.size() * 4;
        return StartingProgramAddress + data.size() * 4;
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
        initializeData();

    }

    private void initializeData() {
        Iterator<Code> iterator = code.iterator();
        boolean proceed = false;

        while (iterator.hasNext()) {
            Code next = iterator.next();
            String currentLine = next.getLine();
            if (currentLine.contains(".data"))
                proceed = true;


        }
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
            return Integer.toHexString(memoryLocation).toUpperCase();
        }

        @Override
        public String toString() {
            return getMemoryLocationHex() + ": " + line;
        }
    }

    public static ProgramCode readFile(String filename) {
        ProgramCode programCode = new ProgramCode();

        // Read file
        try {
            File file = new File(filename);
            System.out.println(file.getAbsolutePath());
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {

                String line = scanner.nextLine();
                line = line.trim();

                // Ignore comments and whitespace
                if (line.startsWith(";") == true) continue;
                if (line.isEmpty() == true) continue;

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
}
