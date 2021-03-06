package dlsu.advcarc.server;

import com.sun.net.httpserver.HttpServer;
import dlsu.advcarc.memory.MemoryManager;
import dlsu.advcarc.parser.StringBinary;
import io.vertx.core.Vertx;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Created by user on 11/17/2015.
 */
public class MipsServer {

    public static void main(String[] args) {
//        Properties properties = System.getProperties();
//        properties.setProperty("vertx.disableFileCaching", "true");

        // Create an HTTP server which simply returns "Hello World!" to each request.
        Vertx vertx = Vertx.factory.vertx();
        vertx.deployVerticle(new MipsVerticle());
    }
}
