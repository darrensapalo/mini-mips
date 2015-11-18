package dlsu.advcarc.server.handlers;

import dlsu.advcarc.parser.ProgramCode;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

/**
 * Created by user on 11/18/2015.
 */
public class InputCodeHandler implements Handler<Message<String>> {
    @Override
    public void handle(Message<String> message) {

        ProgramCode programCode = ProgramCode.readCodeString(message.body());

        message.reply(programCode != null ? programCode.toJsonArray() : false);
    }
}
