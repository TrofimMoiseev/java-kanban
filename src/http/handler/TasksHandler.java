package http.handler;

import http.HttpTaskServer;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.NotFoundException;
import service.TaskManager;
import service.TaskValidationException;

import java.io.IOException;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final int id = getIdFromPath(exchange.getRequestURI().getPath());
        final String response;
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (id == 0) {
                    final List<Task> tasks = taskManager.getTaskList();
                    response = gson.toJson(tasks);
                    System.out.println("Получили все задачи");
                    sendText(exchange, response);
                    return;
                } else {
                    try {
                        Task task = taskManager.getTask(id);
                        response = gson.toJson(task);
                        System.out.println("Получили задачу id = " + id);
                        sendText(exchange, response);
                        return;
                    } catch (NotFoundException e) {
                        System.out.println(e.getMessage());
                        sendNotFound(exchange);
                    }
                }
                break;
            case "POST":
                String json = readText(exchange);
                Task task = gson.fromJson(json, Task.class);
                if (id == 0) {
                    try {
                        int id1 = taskManager.addTask(task);
                        System.out.println(task);
                        System.out.println("Создали задачу id = " + id1);
                        exchange.sendResponseHeaders(201, 0);
                        exchange.close();
                    } catch (TaskValidationException e) {
                        System.out.println(e.getMessage());
                        sendHasInteractions(exchange);
                    }
                } else {
                    try {
                        taskManager.updateTask(task);
                        System.out.println("Обновили задачу id = " + task.getId());
                        exchange.sendResponseHeaders(201, 0);
                        exchange.close();

                    } catch (TaskValidationException e) {
                        System.out.println(e.getMessage());
                        sendHasInteractions(exchange);
                    }
                }
                break;
            case "DELETE":
                try {
                    taskManager.deleteTaskById(id);
                    System.out.println("Удалили задачу id = " + id);
                    exchange.sendResponseHeaders(200, 0);
                    exchange.close();
                } catch (NotFoundException e) {
                    System.out.println(e.getMessage());
                    sendNotFound(exchange);
                }
                break;
            default:
                sendNotFound(exchange);
        }
    }
}
