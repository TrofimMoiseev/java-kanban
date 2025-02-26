package service;

import model.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    private File tempFile;
    private FileBackedTaskManager taskManager;

    @BeforeEach
    void setUp() throws Exception {
        // Создание временного файла для тестов
        tempFile = Files.createTempFile("testFile", ".csv").toFile();
        tempFile.deleteOnExit();
        taskManager = Managers.getDefaultFile(tempFile);
    }

    @Test
    void testSaveAndLoad() throws ManagerSaveException {
        // Создание задачи
        Task task1 = new Task("Task 1", "Description of task", TaskStatus.NEW);
        Epic epic1 = new Epic("Epic 1", "Description of epic");
        Task task2 = new Task("Task 2", "Description of task", TaskStatus.NEW);
        taskManager.addTask(task1);
        int epicId1 = taskManager.addEpic(epic1);
        taskManager.addTask(task2);
        Subtask subtask1 = new Subtask("Subtask 1", "Description of task",
                TaskStatus.IN_PROGRESS, epicId1);
        int subtaskId1 = taskManager.addSubtask(subtask1);
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(taskManager.getTaskList(), loadedManager.getTaskList(), "Tasks должны быть одинаковыми");
        assertEquals(taskManager.getEpicList(), loadedManager.getEpicList(), "SubTasks должны быть одинаковыми");
        assertEquals(taskManager.getSubtaskList(), loadedManager.getSubtaskList(), "Epics должны быть одинаковыми");
        assertEquals(loadedManager.getSubtask(subtaskId1).getEpicId(), epicId1, "Поля должны совпадать");
        assertEquals(taskManager.getEpic(epicId1).getSubtaskList(), loadedManager.getEpic(epicId1).getSubtaskList(),
                "Субтакслисты должны совпадать");
        assertEquals(TaskStatus.IN_PROGRESS, loadedManager.getEpic(epicId1).getStatusOfTask(),
                "Статусы должны совпадать");



    }

    @Test
    void testLoadFromFile() throws ManagerSaveException {
        // Загружаем данные с пустого файла
        taskManager.save();
        taskManager = Managers.getDefaultFile(tempFile);

        assertEquals(0, taskManager.getHistory().size(), "История должна быть пуста");
    }

    @Test
    void testAddTask() {
        Task task = new Task("New Task", "Task description", TaskStatus.NEW);
        task.setId(1);

        // Добавление задачи
        int taskId = taskManager.addTask(task);

        // Проверка, что задача была добавлена
        assertEquals(1, taskId, "ID задачи должен быть равен 1");
        assertNotNull(taskManager.getTask(1), "Задача должна быть добавлена в менеджер");
    }

    @Test
    void testDeleteTask() throws ManagerSaveException {
        Task task = new Task("Task to delete", "Delete description", TaskStatus.NEW);
        task.setId(1);

        // Добавление задачи
        taskManager.addTask(task);

        // Удаление задачи
        taskManager.deleteTaskById(1);

        // Проверка, что задача была удалена
        assertNull(taskManager.getTask(1), "Задача должна быть удалена");
    }

    @AfterEach
    void tearDown() {
        // Удаляем временный файл после выполнения тестов
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }
}