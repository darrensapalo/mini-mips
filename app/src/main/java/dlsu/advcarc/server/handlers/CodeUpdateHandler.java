package dlsu.advcarc.server.handlers;

import dlsu.advcarc.cpu.ExecutionManager;
import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.parser.ProgramCode;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

/**
 * Created by user on 11/18/2015.
 */
public class CodeUpdateHandler implements Handler<Message<String>> {
    @Override
    public void handle(Message<String> message) {

        try {
            ProgramCode programCode = ProgramCode.readCodeString(message.body());
            if(programCode == null)
                throw new Exception("Invalid code: "+message.body());

            ExecutionManager.instance().inputProgramCode(programCode);
            message.reply(true);
            
        }catch(Exception e){
            e.printStackTrace();
            message.reply(false);
        }
    }
}
