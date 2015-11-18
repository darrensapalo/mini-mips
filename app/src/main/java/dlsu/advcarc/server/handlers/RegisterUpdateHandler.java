package dlsu.advcarc.server.handlers;

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
public class RegisterUpdateHandler implements Handler<Message<JsonObject>>{
    @Override
    public void handle(Message<JsonObject> message) {

        String registerName = message.body().getString("name");
        String newValueString = message.body().getString("value");
//        System.out.println(registerName +" and "+newValueString+" update received.");

        try {

            StringBinary newValue;

            if(Register.getType(registerName).equals("R")) {
                long longValue = Long.parseLong(newValueString);
                newValue = RadixHelper.convertLongToStringBinary(longValue);
            }
            else{
                double doubleValue = Double.parseDouble(newValueString);
                long longValue = Double.doubleToLongBits(doubleValue);
                newValue = RadixHelper.convertLongToStringBinary(longValue);
            }

            RegisterManager.instance().updateRegister(registerName, newValue);

            //verify if the value was indeed changed
            if(!RegisterManager.instance().getInstance(registerName).getValue().toHexString().equals(newValue.toHexString()))
                throw new Exception();

            message.reply(true);
        }catch(Exception e){
            e.printStackTrace();
            message.reply(false);
        }
    }
}
