package dlsu.advcarc.server.handlers;

import dlsu.advcarc.memory.MemoryManager;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

/**
 * Created by user on 11/19/2015.
 */
public class MemoryRequestHandler implements Handler<Message<String>>{


    @Override
    public void handle(Message<String> message) {

        String type = message.body() != null ? message.body() : "";

        switch(type){
            case "code": message.reply(MemoryManager.instance().getCodeJsonArray()); break;
            default: message.reply(MemoryManager.instance().getDataJsonArray()); break;
        }

    }
}
