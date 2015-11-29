package dlsu.advcarc.server.handlers;

import dlsu.advcarc.cpurevised.ExecutionManager;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;


/**
 * Created by admin on 11/21/2015.
 */
public class CPUClockHandler implements Handler<Message<Boolean>>{

    @Override
    public void handle(Message<Boolean> message) {
        if(message.body()){ // clock fully
            ExecutionManager.instance().clockFully();
        }
        else
            ExecutionManager.instance().clockOnce();
    }
}
