package io.vertx.blog.first;

import controllers.BankDetailsController;
import controllers.HealthCheckController;
import dao.BasicDao;
import io.vertx.core.Future;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.BodyHandler;
import io.vertx.rxjava.ext.web.handler.CorsHandler;
import utils.UtilityFunctions;

public class MyFirstVerticle extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);

//        BasicDao.createJDBCClient(config());

        prepareRoutes(router);

        // Start the server
        HttpServerOptions options = new HttpServerOptions();
        options.setCompressionSupported(true);

        vertx.createHttpServer().requestHandler(req -> req.response().end("Hello World!"))
                .listen(
                        Integer.getInteger("http.port"), System.getProperty("http.address", "0.0.0.0"));
        //HttpServer server = vertx.createHttpServer(options);
       // System.out.println("server = " + server);
        //server.requestHandler(router::accept).listen(config().getInteger("port"));
//        server.requestHandler(router::accept).listen(config().getInteger("port"));
//	 server.requestHandler(req -> req.response().end("hello world"))
//             .listen(Integer.getInteger("http.port"), System.getProperty("http.address", "0.0.0.0"));

        super.start(startFuture);
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }

    private void prepareRoutes(Router router) {

        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.PUT)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("X-PINGARUNER")
                .allowedHeader("Content-Type"));

        // Get consolidated body for POST, PUT and PATCH calls
        router.post().handler(BodyHandler.create());
        router.patch().handler(BodyHandler.create());
        router.put().handler(BodyHandler.create());
        router.delete().handler(BodyHandler.create());

        // Log all the API calls with request body and query params.
        logRequest(router);
        handleFailure(router);
        router.get("/health_check").handler(HealthCheckController::healthCheck);
        router.get("/bank/details").handler(BankDetailsController::getBankDetails);
        router.get("/bank/list").handler(BankDetailsController::getBankList);
    }

    private void handleFailure(Router router) {
        router.route().failureHandler(context -> {
            Throwable exception = context.failure();
            System.out.println("Exception" + exception);
                UtilityFunctions.sendFailure(context.request().response(), exception.getMessage(),
                        HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
//            }
        });
    }

    private void logRequest(Router router) {
        router.route().handler(routingContext -> {
            System.out.println("Started API : " + routingContext.request().method() +
                    " " + routingContext.request().path());
            if (routingContext.getBodyAsString() != null) {
                System.out.println("Request body is " + routingContext.getBodyAsString());
            }

            if (routingContext.request() != null && routingContext.request().query() != null) {
                System.out.println("Request query are " + routingContext.request().query());
            }
            routingContext.next();
        });
    }
} 
