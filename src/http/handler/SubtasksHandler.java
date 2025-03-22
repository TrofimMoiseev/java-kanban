package http.handler;

import http.HttpTaskServer;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Subtask;
import service.NotFoundException;
import service.TaskManager;
import service.TaskValidationException;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtasksHandler(TaskManager taskManager) {
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
                    final List<Subtask> subtasks = taskManager.getSubtaskList();
                    response = gson.toJson(subtasks);
                    System.out.println("Получили все подзадачи");
                    sendText(exchange, response);
                } else {
                    try {
                        Subtask subtask = taskManager.getSubtask(id);
                        response = gson.toJson(subtask);
                        System.out.println("Получили подзадачу id = " + id);
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
                Subtask subtask = gson.fromJson(json, Subtask.class);
                if (id == 0) {
                    try {
                         int id1 = taskManager.addSubtask(subtask);
                        System.out.println("Создали подзадачу id = " + id1);
                        exchange.sendResponseHeaders(201, 0);
                        exchange.close();
                    } catch (TaskValidationException | NotFoundException e) {
                        System.out.println(e.getMessage());
                        sendHasInteractions(exchange);
                    }
                } else {
                    try {
                        taskManager.updateSubtask(subtask);
                        System.out.println("Обновили подзадачу id = " + subtask.getId());
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
                    taskManager.deleteSubtaskById(id);
                    System.out.println("Удалили подзадачу id = " + id);
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
