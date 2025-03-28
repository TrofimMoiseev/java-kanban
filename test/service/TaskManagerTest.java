package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static model.TaskStatus.IN_PROGRESS;
import static model.TaskStatus.NEW;
import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected TaskManager taskManager;
    protected Task task;
    protected int taskId;
    protected Task savedTask;
    protected Epic epic;
    protected int epicId;
    protected Epic savedEpic;
    protected Subtask subtask;
    protected int subtaskId;
    protected Subtask savedSubtask;
    protected Duration duration;


    protected void initTasks() {
        duration = Duration.ofMinutes(5L);
        task = new Task("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 30, 0));
        epic = new Epic("Test epic", "Test epic description");
    }

    @Test
    void addTask() {
        taskId = taskManager.addTask(task);
        assertThrows(TaskValidationException.class, () -> {
            Task task1 = new Task("Test task", "Test task description", NEW, duration,
                    LocalDateTime.of(2025, 3, 16, 10, 32, 0));
            int taskId1 = taskManager.addTask(task1);
            assertNotNull(taskId, "Задача не найдена.");
            assertEquals(0, taskId1, "Id должен быть 0");
        });
    }


    @Test
    void addSubtask() {
        assertThrows(TaskValidationException.class, () -> {

        epicId = taskManager.addEpic(epic);
        subtask = new Subtask("Test subtask", "Test subtask description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 0, 0), epicId);
        subtaskId = taskManager.addSubtask(subtask);
        Subtask subtask1 = new Subtask("Test subtask", "Test subtask description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 2, 0), epicId);
        int subtaskId1 = taskManager.addSubtask(subtask1);

        assertNotNull(subtaskId, "Задача не найдена.");
        assertEquals(0, subtaskId1, "Id должен быть 0");
        });
    }

    @Test
    void addEpic() {
        epicId = taskManager.addEpic(epic);
        Epic epic1 = new Epic("Test epic", "Test epic description");
        int epicId1 = taskManager.addEpic(epic1);


        assertNotNull(epicId, "Задача не найдена.");
        assertNotNull(epicId1, "Задача не найдена.");
    }

    @Test
    void getTaskList() {
        taskId = taskManager.addTask(task);
        savedTask = taskManager.getTask(taskId);
        List<Task> tasks = taskManager.getTaskList();
        assertNotNull(tasks, "Задачи не возвращаются."); //добавляет задачу и может найти ее по id;
        assertEquals(1, tasks.size(), "Неверное количество задач.");

    }

    @Test
    void getSubtaskList() {
        assertThrows(TaskValidationException.class, () -> {
            epicId = taskManager.addEpic(epic);
            subtask = new Subtask("Test subtask", "Test subtask description", NEW, duration,
                    LocalDateTime.of(2025, 3, 16, 10, 0, 0), epicId);
            subtaskId = taskManager.addSubtask(subtask);
            subtaskId = taskManager.addSubtask(subtask);
            savedSubtask = taskManager.getSubtask(subtaskId);
            List<Subtask> subtasks = taskManager.getSubtaskList();

            assertNotNull(subtasks, "Задачи не возвращаются.");
            assertEquals(1, subtasks.size(), "Неверное количество задач.");
        });
    }

    @Test
    void getEpicList() {
        epicId = taskManager.addEpic(epic);
        savedEpic = taskManager.getEpic(epicId);
        List<Epic> epics = taskManager.getEpicList();


        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
    }

    @Test
    void getPrioritizedTasks() {

    }

    @Test
    void clearTask() {
        task = new Task("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 30, 0));
        Task task1 = new Task("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 40, 0));
        Task task2 = new Task("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 50, 0));
        taskId = taskManager.addTask(task);
        int taskId1 = taskManager.addTask(task1);
        int taskId2 = taskManager.addTask(task2);
        taskManager.clearTask();
        List<Task> tasks = taskManager.getTaskList();

        assertEquals(0, tasks.size(), "Неверное количество задач.");
    }

    @Test
    void clearSubtask() {
        epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 30, 0), epicId);
        Subtask subtask1 = new Subtask("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 40, 0), epicId);
        Subtask subtask2 = new Subtask("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 50, 0), epicId);
        taskId = taskManager.addSubtask(subtask);
        int taskId1 = taskManager.addSubtask(subtask1);
        int taskId2 = taskManager.addSubtask(subtask2);
        taskManager.clearSubtask();
        List<Subtask> subtasks = taskManager.getSubtaskListByEpicId(epicId);
        List<Subtask> subtasks1 = taskManager.getSubtaskList();

        assertEquals(0, subtasks.size(), "Задачи не удаляются.");
        assertEquals(0, subtasks1.size(), "Задачи не удаляются.");
    }

    @Test
    void clearEpic() {
        Epic epic1 = new Epic("Test task", "Test task description");
        Epic epic2 = new Epic("Test task", "Test task description");
        epicId = taskManager.addEpic(epic);
        int epicId1 = taskManager.addEpic(epic1);
        int epicId2 = taskManager.addEpic(epic2);
        taskManager.clearEpic();
        List<Epic> epics = taskManager.getEpicList();


        assertEquals(epics.size(), 0, "Задачи не удаляются.");
    }

    @Test
    void getTask() {
        taskId = taskManager.addTask(task);
        savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task.getNameTask(), savedTask.getNameTask(), "Имена должны совпадать");
        assertEquals(task.getDescriptionTask(), savedTask.getDescriptionTask(), "Описание должны совпадать");
        assertEquals(task.getStatusOfTask(), savedTask.getStatusOfTask(), "Статусы должны совпадать");
        assertEquals(task.getDuration(), savedTask.getDuration(), "Продолжительность должна совпадать");
        assertEquals(task.getStartTime(), savedTask.getStartTime(), "Время начала должны совпадать");


    }

    @Test
    void getSubtask() {
        epicId = taskManager.addEpic(epic);
        subtask = new Subtask("Test subtask", "Test subtask description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 0, 0), epicId);
        subtaskId = taskManager.addSubtask(subtask);
        savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask.getNameTask(), savedSubtask.getNameTask(), "Имена должны совпадать");
        assertEquals(subtask.getDescriptionTask(), savedSubtask.getDescriptionTask(), "Описание должны совпадать");
        assertEquals(subtask.getStatusOfTask(), savedSubtask.getStatusOfTask(), "Статусы должны совпадать");
        assertEquals(subtask.getDuration(), savedSubtask.getDuration(), "Продолжительность должна совпадать");
        assertEquals(subtask.getStartTime(), savedSubtask.getStartTime(), "Время начала должны совпадать");
    }

    @Test
    void getEpic() {
        epicId = taskManager.addEpic(epic);
        savedEpic = taskManager.getEpic(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic.getNameTask(), savedEpic.getNameTask(), "Имена должны совпадать");
        assertEquals(epic.getDescriptionTask(), savedEpic.getDescriptionTask(), "Описание должны совпадать");
    }

    @Test
    void updateTask() {
        taskId = taskManager.addTask(task);
        task.setNameTask("Test task2");
        task.setDescriptionTask("Test task description2");
        task.setStatusOfTask(IN_PROGRESS);
        taskManager.updateTask(task);
        savedTask = taskManager.getTask(taskId);

        assertEquals("Test task2", savedTask.getNameTask(), "Имена задач должны совпадать");
        assertEquals("Test task description2", savedTask.getDescriptionTask(), "Описание задач не должно совпадать");
        assertEquals(IN_PROGRESS, savedTask.getStatusOfTask(), "Статусы задач не должны совпадать");
    }

    @Test
    void updateSubtask() {
        epicId = taskManager.addEpic(epic);
        subtask = new Subtask("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 30, 0), epicId);
        subtaskId = taskManager.addSubtask(subtask);
        subtask.setNameTask("Test task2");
        subtask.setDescriptionTask("Test task description2");
        subtask.setStatusOfTask(IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        savedTask = taskManager.getSubtask(subtaskId);

        assertEquals("Test task2", savedTask.getNameTask(), "Имена не должны совпадать");
        assertEquals("Test task description2", savedTask.getDescriptionTask(), "Описание не должны совпадать");
        assertEquals(IN_PROGRESS, savedTask.getStatusOfTask(), "Статусы не должны совпадать");
    }

    @Test
    void updateEpic() {
        epicId = taskManager.addEpic(epic);
        epic.setNameTask("Test task2");
        epic.setDescriptionTask("Test task2 description");
        taskManager.updateEpic(epic);
        savedTask = taskManager.getEpic(epicId);


        assertEquals(savedTask.getNameTask(), "Test task2", "Имена должны совпадать");
        assertEquals(savedTask.getDescriptionTask(), "Test task2 description", "Описание должны совпадать");
    }

    @Test
    void deleteTaskById() {
        Task task1 = new Task("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 40, 0));
        Task task2 = new Task("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 50, 0));
        taskId = taskManager.addTask(task);
        int taskId1 = taskManager.addTask(task1);
        int taskId2 = taskManager.addTask(task2);
        taskManager.deleteTaskById(taskId1);
        List<Task> tasks = taskManager.getTaskList();


        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
    }

    @Test
    void deleteSubtaskById() {
        epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 30, 0), epicId);
        Subtask subtask1 = new Subtask("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 40, 0), epicId);
        Subtask subtask2 = new Subtask("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 50, 0), epicId);
        subtaskId = taskManager.addSubtask(subtask);
        int subtaskId1 = taskManager.addSubtask(subtask1);
        int subtaskId2 = taskManager.addSubtask(subtask2);
        taskManager.deleteSubtaskById(subtaskId1);
        List<Subtask> tasks = taskManager.getSubtaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
    }

    @Test
    void deleteEpicById() {
        Epic epic1 = new Epic("Test epic", "Test epic description");
        Epic epic2 = new Epic("Test epic", "Test epic description");
        epicId = taskManager.addEpic(epic);
        int epicId1 = taskManager.addEpic(epic1);
        int epicId2 = taskManager.addEpic(epic2);
        taskManager.deleteEpicById(epicId1);
        List<Epic> tasks = taskManager.getEpicList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
    }

    @Test
    void getSubtaskListByEpicId() {
        epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 30, 0), epicId);
        Subtask subtask1 = new Subtask("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 40, 0), epicId);
        Subtask subtask2 = new Subtask("Test task", "Test task description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 50, 0), epicId);
        subtaskId = taskManager.addSubtask(subtask);
        int subtaskId1 = taskManager.addSubtask(subtask1);
        int subtaskId2 = taskManager.addSubtask(subtask2);
        List<Subtask> tasks = taskManager.getSubtaskListByEpicId(epicId);

        assertNotNull(tasks, "Задачи не возвращаются."); //добавляет задачу и может найти ее по id;
        assertEquals(3, tasks.size(), "Неверное количество задач.");
    }

    @Test
    void getHistory() {
        taskId = taskManager.addTask(task);
        savedTask = taskManager.getTask(taskId);
        epicId = taskManager.addEpic(epic);
        savedEpic = taskManager.getEpic(epicId);
        subtask = new Subtask("Test subtask", "Test subtask description", NEW, duration,
                LocalDateTime.of(2025, 3, 16, 10, 50, 0), epicId);
        subtaskId = taskManager.addSubtask(subtask);
        savedSubtask = taskManager.getSubtask(subtaskId);
        List<Task> history = taskManager.getHistory();
        assertNotNull(history);
    }
}