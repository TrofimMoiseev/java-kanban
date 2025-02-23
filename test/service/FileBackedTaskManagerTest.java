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
        tempFile = Files.createTempFile("testFile", ".txt").toFile();
        tempFile.deleteOnExit(); // Удалим файл по завершению тестов
        taskManager = Managers.getFile(tempFile);
    }

    @Test
    void testSaveAndLoad() throws ManagerSaveException, IOException {
        // Создание задачи
        Task task = new Task("Task 1", "Description of task", TaskStatus.NEW);
        Epic epic = new Epic("Test epic", "Test epic description");
        Task task1 = new Task("Task 2", "Description of task", TaskStatus.NEW);
        // Добавление задачи и сохранение в файл
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addTask(task1);
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getEpic(2));
        System.out.println(taskManager.getTask(3));
        // Создание нового менеджера и загрузка данных из файла
        FileBackedTaskManager loadedManager = Managers.getFile(tempFile);
        System.out.println(loadedManager.getTask(1));
        System.out.println(loadedManager.getEpic(2));
        System.out.println(taskManager.getTask(3));

        // Проверка, что задача была сохранена и загружена
        assertNotNull(loadedManager.getTask(1), "Задача должна быть загружена из файла");
        assertNotNull(loadedManager.getEpic(2), "Задача должна быть загружена из файла");
        assertNotNull(loadedManager.getTask(3), "Задача должна быть загружена из файла");
    }

    @Test
    void testLoadFromFile() throws ManagerSaveException, IOException {
        // Загружаем данные с пустого файла
        taskManager.save();
        taskManager = Managers.getFile(tempFile);

        assertEquals(0, taskManager.getHistory().size(), "История должна быть пуста");
    }

    @Test
    void testAddTask() throws ManagerSaveException {
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
    void tearDown() throws IOException {
        // Удаляем временный файл после выполнения тестов
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }
}