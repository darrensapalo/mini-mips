package dlsu.advcarc.server.handlers;

import dlsu.advcarc.cpurevised.ExecutionManager;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

/**
 * Created by user on 11/22/2015.
 */
public class CPURequestHandler implements Handler<Message<Object>>{
    @Override
    public void handle(Message<Object> message) {
        message.reply(ExecutionManager.instance().getCPU().toJsonObject());
    }
}
