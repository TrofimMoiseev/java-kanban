package service;

import java.io.File;
import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static FileBackedTaskManager getFile(File file) throws IOException, ManagerSaveException {
        return new FileBackedTaskManager(getDefaultHistory(), file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
