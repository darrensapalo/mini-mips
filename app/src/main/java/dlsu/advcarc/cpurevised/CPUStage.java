package dlsu.advcarc.cpurevised;

import io.vertx.core.json.JsonArray;

/**
 * Created by user on 11/29/2015.
 */
public interface CPUStage {

    public boolean hasInstructionToForward();

    public boolean execute();

    public JsonArray toJsonArray();

    public void housekeeping(CPUStage previousStage);

    public void resetToNOP();

}
