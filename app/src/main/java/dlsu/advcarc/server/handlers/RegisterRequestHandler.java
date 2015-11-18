package dlsu.advcarc.server.handlers;

import dlsu.advcarc.parser.Register;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

/**
 * Created by user on 11/18/2015.
 */
public class RegisterRequestHandler implements Handler<Message<Object>> {
    @Override
    public void handle(Message message) {
        System.out.println(Register.getJsonArrays().toString());
        message.reply(Register.getJsonArrays());
    }
}
