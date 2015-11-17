package dlsu.advcarc.server;

import com.sun.net.httpserver.HttpServer;
import dlsu.advcarc.server.handlers.HttpRequestHandler;
import io.vertx.core.Vertx;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Created by user on 11/17/2015.
 */
public class MipsServer {

    public static void main(String[] args) {
        // Create an HTTP server which simply returns "Hello World!" to each request.
        Vertx vertx = Vertx.factory.vertx();
        vertx.deployVerticle(new MipsVerticle());
    }
}
