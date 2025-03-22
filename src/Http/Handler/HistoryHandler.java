package Http.Handler;

import Http.HttpTaskServer;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
       if(exchange.getRequestMethod().equals("GET")) {
           final List<Task> history = taskManager.getHistory();
           response = gson.toJson(history);
           System.out.println("Получили историю");
           sendText(exchange, response);
       } else {
           sendNotFound(exchange);
       }
    }
}