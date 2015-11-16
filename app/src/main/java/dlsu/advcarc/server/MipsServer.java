package dlsu.advcarc.server;

import com.sun.net.httpserver.HttpServer;
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
        Vertx.factory.vertx().createHttpServer().requestHandler(req -> req.response().end("Hello World!")).listen(8080);
    }



//
//    @Override
//    public void start(){
//        String portProperty = System.getProperty("pisara.port");
//        int port = 8888;
//        try{
//            port = Integer.parseInt(portProperty);
//        }catch(Exception e){}
//        HttpServer server = vertx.createHttpServer();
//        server.setMaxWebSocketFrameSize(Integer.MAX_VALUE);
//        server.requestHandler(new HttpRequestHandler(vertx, activeUsers));
//
//        JsonArray permitted = new JsonArray();
//        permitted.add(new JsonObject()); // Let everything through
//        JsonObject config = new JsonObject();
//        config.putString("prefix", "/eventbus");
//
//        ServerHook hook = new ServerHook(boardTracker);
//
//        SockJSServer sockJSServer = vertx.createSockJSServer(server);
//        sockJSServer.setHook(hook);
//        sockJSServer.bridge(config, permitted, permitted);
//
//        registerHandlers();
//
//        server.listen(port);
//    }
//
//    private void registerHandlers(){
//
//        vertx.eventBus().registerHandler("")
//
//        vertx.eventBus().registerHandler(Addresses.USER_VALIDATION_ADDRESS, new UserValidationHandler(boardTracker, vertx));
//
//        Properties prop = new Properties();
//        InputStream input = null;
//        try {
//            input = new FileInputStream("config.properties");
//            prop.load(input);
//            password = prop.getProperty("adminpassword");
//        }
//        catch (IOException ex) {}
//        finally {
//            if (input != null) {
//                try {
//                    input.close();
//                } catch (IOException e) {}
//            }
//        }
//        vertx.eventBus().registerHandler(Addresses.ADMIN_ADDRESS, new AdminHandler(boardTracker, password));
//    }
//
//
//    private void registerCanvasHandlers(){
//        vertx.eventBus().registerHandler(Addresses.CANVAS_REQUEST_ADDRESS, new CanvasRequestHandler(boardTracker, vertx));
//    }


}
