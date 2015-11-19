package dlsu.advcarc.parser;

import dlsu.advcarc.memory.Memory;
import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.parser.Code;
import dlsu.advcarc.utils.RadixHelper;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;


/**
 * Created by Darren on 11/9/2015.
 */
public class ProgramCode {
    private LinkedList<Code> code = new LinkedList<>();
    private LinkedList<Code> data = new LinkedList<>();

    private String codeString;
    private String parsingErrors;

    private static int StartingMemoryAddress = 0;
    private static int StartingProgramAddress = 0;

    private Section currentSection;

    public ProgramCode(String codeString) {
        currentSection = Section.Program;
        this.codeString = codeString;
    }

    public enum Section {
        Data, Program
    }

    public void addInstruction(String line) {
        line = line.trim();
        code.add(new Code(line, getNextAvailableAddress()));
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

    public void setParsingErrors(String parsingErrors) {
        this.parsingErrors = parsingErrors;
    }

    public boolean isValid() {
        return parsingErrors == null || parsingErrors.isEmpty();
    }

    public String getParsingErrors() {
        return parsingErrors;
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

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("text", codeString.replaceAll(".text", "").trim());
        jsonObject.put("array", toJsonArray(true));
        return jsonObject;
    }

    public JsonArray toJsonArray(boolean includeInstruction) {
        JsonArray jsonArray = new JsonArray();
        for (Code codeEntry : code) {
            jsonArray.add(codeEntry.toJsonObject(includeInstruction/**/));
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
            Memory instance = MemoryManager.instance().getInstance(memoryLocationHex);
            String value = split[1];

            instance.write(value);
        }
        // use iterator, initialize memory modules
    }

}
