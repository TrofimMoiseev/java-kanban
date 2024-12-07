package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static model.TaskStatus.NEW;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    TaskManager taskManager;
    Task task;
    Epic epic;
    Subtask subtask;
    int taskId;
    int epicId;
    int subtaskId;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        task = new Task("Test task", "Test task description", NEW);
        taskId = taskManager.addNewTask(task);
        epic = new Epic("Test epic", "Test epic description");
        epicId = taskManager.addNewEpic(epic);
        subtask = new Subtask("Test task", "Test task description", NEW, epicId);
        subtaskId = taskManager.addNewSubtask(subtask);
    }

    @Test
    void add() {
        taskManager.getTask(taskId);
        List<Task> testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(1, testList.size(), "Неверное количество задач.");

        taskManager.getEpic(epicId);
        testList = taskManager.getHistory();
        assertEquals(2, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), task, "Неправильный порядок");
        assertEquals(testList.get(1), epic, "Неправильный порядок");

        taskManager.getSubtask(subtaskId);
        testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(3, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), task, "Неправильный порядок");
        assertEquals(testList.get(1), epic, "Неправильный порядок");
        assertEquals(testList.get(2), subtask, "Неправильный порядок");


    }

    @Test
    void deleteEpic() {
        taskManager.getTask(taskId);
        taskManager.getEpic(epicId);
        taskManager.getSubtask(subtaskId);
        taskManager.deleteEpicById(epicId);
        List<Task> testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(1, testList.size(), "Неверное количество задач.");
    }

    @Test
    void deleteTask() {
        taskManager.getTask(taskId);
        taskManager.getEpic(epicId);
        taskManager.getSubtask(subtaskId);
        taskManager.deleteTaskById(taskId);
        List<Task> testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(2, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), epic, "Неправильный порядок");
        assertEquals(testList.get(1), subtask, "Неправильный порядок");
    }

    @Test
    void deleteSubtask() {
        taskManager.getTask(taskId);
        taskManager.getEpic(epicId);
        taskManager.getSubtask(subtaskId);
        taskManager.deleteSubtaskById(subtaskId);
        List<Task> testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(2, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), task, "Неправильный порядок");
        assertEquals(testList.get(1), epic, "Неправильный порядок");
    }

    @Test
    void getHistory() {
        taskManager.getTask(taskId);
        taskManager.getEpic(epicId);
        taskManager.getSubtask(subtaskId);
        List<Task> testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(3, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), task, "Неправильный порядок");
        assertEquals(testList.get(1), epic, "Неправильный порядок");
        assertEquals(testList.get(2), subtask, "Неправильный порядок");

        taskManager.getTask(taskId);
        testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(3, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), epic, "Неправильный порядок");
        assertEquals(testList.get(1), subtask, "Неправильный порядок");
        assertEquals(testList.get(2), task, "Неправильный порядок");

    }
}