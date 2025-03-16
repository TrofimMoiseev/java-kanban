package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ManagerSaveException;
import service.Managers;
import service.TaskManager;

import java.time.LocalDateTime;

import static model.TaskStatus.*;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    TaskManager taskManager = Managers.getDefault();
    Epic epic1;
    int epicId;
    Subtask subtask1;
    int subtask1Id;
    Subtask savedSubtask1;

    @BeforeEach
    void setUp() throws ManagerSaveException {
        epic1 = new Epic("Test epic1", "Test epic1 description");
        epicId = taskManager.addEpic(epic1);
        subtask1 = new Subtask("Test subtask1", "Test subtask1 description", NEW, 5L, LocalDateTime.of(2025, 3, 16, 10, 30, 0), epicId);
        subtask1Id = taskManager.addSubtask(subtask1);
        savedSubtask1 = taskManager.getSubtask(subtask1Id);
    }

    @Test
    void subtaskComparisonsById() throws ManagerSaveException {
        subtask1.setNameTask("Test subtask2");
        subtask1.setDescriptionTask("Test subtask2 description");
        subtask1.setStatusOfTask(IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        Subtask savedSubtask2 = taskManager.getSubtask(subtask1Id);

        assertEquals(savedSubtask2, savedSubtask1, "Задачи не совпадают.");
    }

    @Test
    void checkSubtaskCannotAddItselfAsSubtask() throws ManagerSaveException {
        Task subtask2 = new Subtask("Test subtask1", "Test subtask1 description", NEW , 5L, LocalDateTime.of(2025, 3, 16, 10, 30, 0), subtask1Id);
        int subtask2Id = taskManager.addSubtask((Subtask) subtask2);
        assertEquals(0,subtask2Id,"Ошибка, субтаск добавлен!");
    }
}