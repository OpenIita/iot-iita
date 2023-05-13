package cc.iotkit.screen.staticres;

import cc.iotkit.screen.api.ScreenApiHandle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author：tfd
 * @Date：2023/5/10 10:30
 */
@Slf4j
public class ScreenVerticle extends AbstractVerticle {

    private HttpServer httpServer;

    private int port;

    private ScreenApiHandle apiHandler;

    public ScreenVerticle(int port) {
        this.port = port;
    }

    public void setApiHandler(ScreenApiHandle apiHandler) {
        this.apiHandler = apiHandler;
    }

    @Override
    public void start() throws Exception {
        httpServer=vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route("/iotkit/screen/*").handler(StaticHandler.create("G:/OpenSourceCode/mainCode/iotkit-parent/data/screens/908b89d3-0c2e-4347-a71a-cf356ccc6273"));
        router.get("/iotkit/screen").handler(ctx -> {
            ctx.response().sendFile("G:/OpenSourceCode/mainCode/iotkit-parent/data/screens/908b89d3-0c2e-4347-a71a-cf356ccc6273/index.html");
        });
        router.get("/*").handler(ctx -> {
            String res=apiHandler.httpReq(ctx.request(),ctx.response());
            ctx.response().end(res);
        });
        router.post("/*").handler(BodyHandler.create()).handler(ctx -> {
            String res=apiHandler.httpReq(ctx.request(),ctx.response());
            ctx.response().end(res);
        });
        httpServer.requestHandler(router).listen(port);
    }

    @Override
    public void stop() throws Exception {
        httpServer.close(voidAsyncResult -> log.info("close httpServer server..."));
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ScreenVerticle(2222));
        System.out.println("服务器已启动，请访问 http://localhost:2222");
    }
}
