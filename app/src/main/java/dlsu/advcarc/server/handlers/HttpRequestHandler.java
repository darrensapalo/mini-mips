package dlsu.advcarc.server.handlers;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.Handler;

/**
 * Created by user on 11/17/2015.
 */
public class HttpRequestHandler implements Handler<HttpServerRequest> {

    Vertx vertx;

    public HttpRequestHandler(Vertx vertx){
        this.vertx = vertx;
    }

    @Override
    public void handle(final HttpServerRequest request) {
        System.out.println("Got a request with path: "+request.path()+" and "+request.absoluteURI());
        if(request.path().equals("/")) {
            request.response().sendFile("web/index.html");
        }
        else{
            request.response().sendFile("web"+request.path());
        }
    }

}
