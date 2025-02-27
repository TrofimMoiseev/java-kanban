package service;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefault() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
    }

    @Test
    void getDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
    }

    @Test
    void getFile() throws IOException {
        File tempFile = Files.createTempFile("testFile", ".txt").toFile();
        tempFile.deleteOnExit();
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFile(tempFile);
        assertNotNull(fileBackedTaskManager);
    }
}