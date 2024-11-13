package service;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new ArrayList<>();


    @Override
    public void addToHistory(Task task) {
        if (task == null){
            return;
        }

        if (history.size() >= 10){
           history.removeFirst();
        }
        Task cloneTask = new Task(task.getNameTask(), task.getDescriptionTask(), task.getStatusOfTask());
        cloneTask.setId(task.getId());
        history.add(cloneTask);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
