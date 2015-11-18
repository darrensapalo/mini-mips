package dlsu.advcarc.server;

import com.sun.jndi.cosnaming.IiopUrl;
import dlsu.advcarc.parser.ProgramCode;
import dlsu.advcarc.server.handlers.InputCodeHandler;
import dlsu.advcarc.server.handlers.RegisterRequestHandler;
import dlsu.advcarc.server.handlers.RegisterUpdateHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 11/18/2015.
 */
public class MipsVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route("/eventbus/*").handler(SockJSHandler.create(vertx)
                .bridge(createBridgePermissions()));
        router.route().handler(StaticHandler.create("web"));

        registerEventBusHandlers();

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private void registerEventBusHandlers(){
        vertx.eventBus().consumer(Addresses.CODE_INPUT, new InputCodeHandler());
        vertx.eventBus().consumer(Addresses.REGISTER_REQUEST, new RegisterRequestHandler());
        vertx.eventBus().consumer(Addresses.REGISTER_UPDATE, new RegisterUpdateHandler());

    }

    private BridgeOptions createBridgePermissions(){
        List<PermittedOptions> permittedOptionsList = new ArrayList<PermittedOptions>();

        List<String> addresses = Addresses.getAllAddresses();

        for(String address: addresses)
            permittedOptionsList.add(new PermittedOptions().setAddress(address));

        BridgeOptions bridgeOptions = new BridgeOptions();
        for(PermittedOptions permittedOption: permittedOptionsList) {
            bridgeOptions.addOutboundPermitted(permittedOption);
            bridgeOptions.addInboundPermitted(permittedOption);
        }

        return bridgeOptions;
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Verticle stopped");
    }

}
