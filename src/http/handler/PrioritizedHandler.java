package http.handler;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        if (exchange.getRequestMethod().equals("GET")) {
            final List<Task> prioritizedList = taskManager.getPrioritizedTasks();
            response = gson.toJson(prioritizedList);
            System.out.println("Получили сортированный список");
            sendText(exchange, response);
        } else {
            sendNotFound(exchange);
        }
    }
}
