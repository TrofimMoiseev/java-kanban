package Http.Handler;

import Http.HttpTaskServer;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        if(exchange.getRequestMethod().equals("GET")) {
            final List<Task> prioritizedList = taskManager.getPrioritizedTasks();
            response = gson.toJson(prioritizedList);
            System.out.println("Получили сортированный список");
            sendText(exchange, response);
        } else {
            sendNotFound(exchange);
        }
    }
}
