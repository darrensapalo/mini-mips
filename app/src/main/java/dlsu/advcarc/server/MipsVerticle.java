package dlsu.advcarc.server;

import dlsu.advcarc.server.handlers.HttpRequestHandler;
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

        /* Inbound/Outbound Traffic Permissions */
        List<PermittedOptions> permittedOptionsList = new ArrayList<PermittedOptions>();
        permittedOptionsList.add(new PermittedOptions().setAddress(Addresses.CODE_INPUT));

        BridgeOptions bridgeOptions = new BridgeOptions();
        for(PermittedOptions permittedOption: permittedOptionsList) {
            bridgeOptions.addOutboundPermitted(permittedOption);
            bridgeOptions.addInboundPermitted(permittedOption);
        }

        router.route("/eventbus/*").handler(SockJSHandler.create(vertx)
                .bridge(bridgeOptions));

        router.route().handler(StaticHandler.create("web"));

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

        vertx.eventBus().consumer(Addresses.CODE_INPUT, message -> {
            System.out.println("Received message.body() = "
                    + message.body());
        });
    }

    @Override
    public void stop() throws Exception {
        System.out.println("BasicVerticle stopped");
    }

}
