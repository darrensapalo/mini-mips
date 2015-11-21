package dlsu.advcarc.server;

import io.vertx.core.eventbus.EventBus;

/**
 * Created by user on 11/20/2015.
 */
public class EventBusHolder {

    private static EventBusHolder instance;
    public static EventBusHolder instance(){
        if(instance == null)
            instance = new EventBusHolder();
        return instance;
    }


    private EventBus eventBus;

    public void setEventBus(EventBus eb){
        eventBus = eb;
    }

    public EventBus getEventBus(){
        return eventBus;
    }




}
