package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ManagerSaveException;
import service.Managers;
import service.TaskManager;

import java.time.LocalDateTime;

import static model.TaskStatus.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    TaskManager taskManager = Managers.getDefault();
    Task task1;
    Task savedTask;
    int taskId;

    @BeforeEach
    void setUp() throws ManagerSaveException {
        task1 = new Task("Test task1", "Test task1 description", NEW, 5L, LocalDateTime.of(2025, 3, 16, 10, 30, 0));
        taskId = taskManager.addTask(task1);
        savedTask = taskManager.getTask(taskId);
    }

    @Test
    void taskComparisonsById() throws ManagerSaveException {
        task1.setNameTask("Test task2");
        task1.setDescriptionTask("Test task2 description");
        task1.setStatusOfTask(IN_PROGRESS);
        taskManager.updateTask(task1);
        Task savedTask1 = taskManager.getTask(taskId);

        assertEquals(savedTask1, savedTask, "Задачи не совпадают.");
    }




}