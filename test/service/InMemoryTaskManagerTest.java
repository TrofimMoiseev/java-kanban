package service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;

import java.util.List;

import static model.TaskStatus.IN_PROGRESS;
import static model.TaskStatus.NEW;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager;
    Task task;
    int taskId;
    Task savedTask;
    Epic epic;
    int epicId;
    Epic savedEpic;
    Subtask subtask;
    int subtaskId;
    Subtask savedSubtask;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        task = new Task("Test task", "Test task description", NEW);
        epic = new Epic("Test epic", "Test epic description");
    }

    @Test
    void addNewTaskTest() {
        taskId = taskManager.addNewTask(task);
        savedTask = taskManager.getTask(taskId);
        assertNotNull(savedTask, "Задача не найдена."); //тест, в котором проверяется неизменность задачи
        assertEquals(task, savedTask, "Задачи не совпадают.");

    }

    @Test
    void getTaskListTest() {
        taskId = taskManager.addNewTask(task);
        savedTask = taskManager.getTask(taskId);
        List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются."); //добавляет задачу и может найти ее по id;
        assertEquals(1, tasks.size(), "Неверное количество задач.");
    }

    @Test
    void addNewEpicTest() {
        epicId = taskManager.addNewEpic(epic);
        savedEpic = taskManager.getEpic(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    void getEpicListTest() {
        epicId = taskManager.addNewEpic(epic);
        savedEpic = taskManager.getEpic(epicId);
        List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
    }

    @Test
    void addNewSubtaskTest() {
        epicId = taskManager.addNewEpic(epic);
        subtask = new Subtask("Test subtask", "Test subtask description", NEW, epicId);
        subtaskId = taskManager.addNewSubtask(subtask);
        savedSubtask = taskManager.getSubtask(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
    }

    @Test
    void getSubtaskListTest() {
        epicId = taskManager.addNewEpic(epic);
        subtask = new Subtask("Test subtask", "Test subtask description", NEW, epicId);
        subtaskId = taskManager.addNewSubtask(subtask);
        savedSubtask = taskManager.getSubtask(subtaskId);
        List<Subtask> subtasks = taskManager.getSubtaskList();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
    }

    @Test
    void historyNotNull() {
        taskId = taskManager.addNewTask(task);
        savedTask = taskManager.getTask(taskId);
        epicId = taskManager.addNewEpic(epic);
        savedEpic = taskManager.getEpic(epicId);
        subtask = new Subtask("Test subtask", "Test subtask description", NEW, epicId);
        subtaskId = taskManager.addNewSubtask(subtask);
        savedSubtask = taskManager.getSubtask(subtaskId);
        List<Task> history = taskManager.getHistory();
        assertNotNull(history);
    }

    @Test
    void saveHistoryAfterUpdate() {
        taskId = taskManager.addNewTask(task);
        savedTask = taskManager.getTask(taskId);
        epicId = taskManager.addNewEpic(epic);
        savedEpic = taskManager.getEpic(epicId);
        subtask = new Subtask("Test subtask", "Test subtask description", NEW, epicId);
        subtaskId = taskManager.addNewSubtask(subtask);
        savedSubtask = taskManager.getSubtask(subtaskId);
        List<Task> history = taskManager.getHistory();
        int historySize1 = history.size();
        task.setNameTask("Test task1");
        task.setDescriptionTask("Test task1 description");
        task.setStatusOfTask(IN_PROGRESS);
        taskManager.updateTask(task);
        epic.setNameTask("Test epic1");
        epic.setDescriptionTask("Test epic1 description");
        taskManager.updateEpic(epic);
        subtask.setNameTask("Test subtask1");
        subtask.setDescriptionTask("Test subtask1 description");
        subtask.setStatusOfTask(IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        savedTask = taskManager.getTask(taskId);
        savedEpic = taskManager.getEpic(epicId);
        savedSubtask = taskManager.getSubtask(subtaskId);
        history = taskManager.getHistory();
        int historySize2 = history.size();

        assertNotEquals(historySize1, historySize2);
        assertNotEquals(history.get(0).getNameTask(), history.get(3).getNameTask(), "Ошибка, экземпляр изменился.");
        assertNotEquals(history.get(0).getDescriptionTask(), history.get(3).getDescriptionTask(), "Ошибка, экземпляр изменился.");
        assertNotEquals(history.get(0).getStatusOfTask(), history.get(3).getStatusOfTask(), "Ошибка, экземпляр изменился.");
        assertNotEquals(history.get(1).getNameTask(), history.get(4).getNameTask(), "Ошибка, экземпляр изменился.");
        assertNotEquals(history.get(1).getDescriptionTask(), history.get(4).getDescriptionTask(), "Ошибка, экземпляр изменился.");
        assertNotEquals(history.get(1).getStatusOfTask(), history.get(4).getStatusOfTask(), "Ошибка, экземпляр изменился.");
        assertNotEquals(history.get(2).getNameTask(), history.get(5).getNameTask(), "Ошибка, экземпляр изменился.");
        assertNotEquals(history.get(2).getDescriptionTask(), history.get(5).getDescriptionTask(), "Ошибка, экземпляр изменился.");
        assertNotEquals(history.get(2).getStatusOfTask(), history.get(5).getStatusOfTask(), "Ошибка, экземпляр изменился.");

    }

}