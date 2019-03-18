package utils;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.http.HttpServerResponse;


public class UtilityFunctions {
    public static void sendFailure(HttpServerResponse httpServerResponse, String reason, int statusCode) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("status", "FAILURE");
        jsonObject.put("reason", reason);
        httpServerResponse.putHeader("content-type", "application/json; charset=utf-8");
        httpServerResponse.putHeader("charset", "UTF-8");
        httpServerResponse.setStatusCode(statusCode);
        httpServerResponse.end(jsonObject.toString());
    }

    public static void sendSuccess(HttpServerResponse httpServerResponse, Object value) {
        JsonObject response = new JsonObject();
        response.put("status", "SUCCESS");
        response.put("data", value);
        httpServerResponse.putHeader("content-type", "application/json; charset=utf-8");
        httpServerResponse.putHeader("charset", "UTF-8");
        httpServerResponse.setStatusCode(HttpResponseStatus.OK.code());


        System.out.println("response = " + response);
        httpServerResponse.end(response.toString());
    }
} 