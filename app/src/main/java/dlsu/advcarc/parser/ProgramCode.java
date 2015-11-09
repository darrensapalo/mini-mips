package dlsu.advcarc.parser;

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
    private static int StartingMemoryAddress = 0;

    public void addInstruction(String line) {
        line = line.trim();
        code.add(new Code(line, getNextAvailableAddress()));
    }

    public LinkedList<Code> getProgram(){
        return code;
    }

    private int getNextAvailableAddress() {
        return StartingMemoryAddress + code.size() * 4;
    }

    public int InitialProgramCounter() {
        return StartingMemoryAddress;
    }

    public void setInitialProgramCounter(int start){
        StartingMemoryAddress = start;
    }

    public String getCode(int programCounter) {
        Iterator<Code> iterator = code.iterator();
        while (iterator.hasNext()){
            Code next = iterator.next();
            if (next.getMemoryLocation() == programCounter)
                return next.getLine();
        }
        return null;
    }

    public class Code {
        private String line;
        private int memoryLocation;

        public Code(String line, int memoryLocation) {
            this.line = line;
            this.memoryLocation = memoryLocation;
        }

        public String getLine() {
            return line;
        }

        public int getMemoryLocation() {
            return memoryLocation;
        }

        @Override
        public String toString() {
            return memoryLocation + ": " + line;
        }
    }

    public static ProgramCode readFile(String filename){
        ProgramCode programCode = new ProgramCode();

        // Read file
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                programCode.addInstruction(scanner.nextLine());
            }
        }catch (FileNotFoundException e){
            throw new IllegalStateException("Could not find input program at " + filename);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return programCode;
    }
}
