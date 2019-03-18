package controllers;

import io.vertx.rxjava.ext.web.RoutingContext;
import utils.UtilityFunctions;

public class HealthCheckController {
    public static void healthCheck(RoutingContext context) {
        UtilityFunctions.sendSuccess(context.response(), "abc");
    }
} 