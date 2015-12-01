package dlsu.advcarc.register;

import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.server.Addresses;
import dlsu.advcarc.server.EventBusHolder;
import dlsu.advcarc.server.MipsVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;

/**
 * Created by user on 11/18/2015.
 */
public class RegisterManager {

    /* Singleton */
    private static RegisterManager instance;

    public static RegisterManager instance() {
        if (instance == null)
            instance = new RegisterManager();
        return instance;
    }


    /* Class attributes and methods  */
    private List<Register> rRegisters = new ArrayList<Register>();
    private List<Register> fRegisters = new ArrayList<Register>();

    private RegisterManager() {
        initRegisters();
    }

    private void initRegisters() {
        for (int i = 0; i < 32; i++) {
            rRegisters.add(new Register("R" + i));
        }

        for (int i = 0; i < 32; i++) {
            fRegisters.add(new Register("F" + i));
        }
    }

    public Register getInstance(String registerName) throws IllegalArgumentException {
        if (!Register.validate(registerName))
            throw new IllegalArgumentException("Immediate name not valid: " + registerName);

        String registerType = Register.getType(registerName);
        int registerNum = Register.getNumber(registerName);

        switch (registerType) {
            case "R":
                return rRegisters.get(registerNum);
            case "F":
                return fRegisters.get(registerNum);
        }

        return null;
    }

    public void updateRegister(String registerName, StringBinary newValue) {

        if("R0".equals(registerName) || "FO".equals(registerName))
            return;

        Register register = getInstance(registerName);
        register.setValue(newValue);

        /* Broadcast the Updated Immediate Values */
        EventBusHolder.instance()
                .getEventBus()
                .publish(Addresses.REGISTER_BROADCAST,
                        RegisterManager.instance().toJsonObject()
                );
    }

    public void clear() {
        rRegisters.clear();
        fRegisters.clear();
        initRegisters();
    }


    /* Json Methods */
    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("r-registers", getJsonArrayR());
        jsonObject.put("f-registers", getJsonArrayF());
        return jsonObject;
    }

    public JsonArray getJsonArrayR() {
        JsonArray rArray = new JsonArray();

        for (int i = 0; i < rRegisters.size(); i++)
            if (i > 0)
                rArray.add(rRegisters.get(i).toJsonObject(16));

        return rArray;
    }

    public JsonArray getJsonArrayF() {
        JsonArray fArray = new JsonArray();

        for (int i = 0; i < fRegisters.size(); i++)
            if (i > 0)
                fArray.add(fRegisters.get(i).toJsonObject(8));

        return fArray;
    }


}
