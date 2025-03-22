package http.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    final static int NUM_PARTS_IN_PATH_WITH_ID = 3;

    protected String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(406, 0);
        exchange.getResponseBody().write("Есть пересечение с текущими задачами".getBytes(StandardCharsets.UTF_8));
        exchange.close();
    }

    protected Integer getIdFromPath(String path) {
        final String[] urlSplited = path.split("/");
        int id = 0;
        if (urlSplited.length >= NUM_PARTS_IN_PATH_WITH_ID) {
            id = Integer.parseInt(urlSplited[NUM_PARTS_IN_PATH_WITH_ID-1]);
        }
        return id;
    }
}
