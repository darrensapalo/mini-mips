package dlsu.advcarc.parser;

import java.util.LinkedList;

/**
 * Created by Darren on 11/9/2015.
 */
public class ProgramCode {
    public LinkedList<Code> code = new LinkedList<>();
    private static int StartingMemoryAddress = 0;

    public void addInstruction(String line) {
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
}
