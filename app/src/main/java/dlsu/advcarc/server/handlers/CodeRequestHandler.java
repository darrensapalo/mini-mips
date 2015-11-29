package dlsu.advcarc.server.handlers;

import dlsu.advcarc.cpurevised.ExecutionManager;
import dlsu.advcarc.parser.ProgramCode;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

/**
 * Created by user on 11/19/2015.
 */
public class CodeRequestHandler implements Handler<Message<Object>> {

    @Override
    public void handle(Message<Object> message) {

        ProgramCode programCode = ExecutionManager.instance().getProgramCode();

        if(programCode != null)
            message.reply(programCode.toJsonObject());
        else
            message.reply(false);
    }
}
