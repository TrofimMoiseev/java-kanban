package http.handler;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        if (exchange.getRequestMethod().equals("GET")) {
            final List<Task> history = taskManager.getHistory();
            response = gson.toJson(history);
            System.out.println("Получили историю");
            sendText(exchange, response);
        } else {
            sendNotFound(exchange);
        }
    }
}