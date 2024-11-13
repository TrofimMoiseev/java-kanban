package service;

import model.*;

import java.util.List;

public interface HistoryManager {

    void addToHistory(Task task);

    List<Task> getHistory();

}
