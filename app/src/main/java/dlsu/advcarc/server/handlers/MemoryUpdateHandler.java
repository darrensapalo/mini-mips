package dlsu.advcarc.server.handlers;

import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.parser.StringBinary;
import dlsu.advcarc.register.Register;
import dlsu.advcarc.register.RegisterManager;
import dlsu.advcarc.utils.RadixHelper;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/19/2015.
 */
public class MemoryUpdateHandler implements Handler<Message<JsonObject>> {

    @Override
    public void handle(Message<JsonObject> message) {
        String memoryLocation = message.body().getString("address");
        String newValueString = message.body().getString("value");

        try {

            StringBinary newValue = RadixHelper.convertHexToStringBinary(newValueString);

//            if (newValueString.contains(".")) {
//                double doubleValue = Double.parseDouble(newValueString);
//                long longValue = Double.doubleToLongBits(doubleValue);
//                newValue = RadixHelper.convertLongToStringBinary(longValue);
//            }
//            else {
//                long longValue = Long.parseLong(newValueString);
//                newValue = RadixHelper.convertLongToStringBinary(longValue);
//            }

            MemoryManager.instance().updateMemory(memoryLocation, newValue);

            //verify if the value was indeed changed
            if(!MemoryManager.instance().getInstance(memoryLocation).getAsHex().equals(newValue.toHexString()))
                throw new Exception();

            message.reply(true);
        } catch (Exception e) {
            e.printStackTrace();
            message.reply(false);
        }

    }
}
