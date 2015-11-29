package dlsu.advcarc.server.handlers;

import dlsu.advcarc.cpurevised.ExecutionManager;
import dlsu.advcarc.parser.MipsParser;
import dlsu.advcarc.parser.ProgramCode;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 11/18/2015.
 */
public class CodeUpdateHandler implements Handler<Message<String>> {
    @Override
    public void handle(Message<String> message) {

        ProgramCode programCode;

        try {
            programCode = MipsParser.parseCodeString(message.body());

            if (programCode.isValid())
                ExecutionManager.instance().inputProgramCode(programCode);

            JsonObject jsonObject = new JsonObject();
            jsonObject.put("isSuccessful", programCode.isValid());
            jsonObject.put("errors", programCode.getParsingErrors());
            message.reply(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();

            // Some generic, unknown error occurred
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("isSuccessful", false);
            jsonObject.put("errors", e.getMessage());
            message.reply(jsonObject);
        }
    }
}
