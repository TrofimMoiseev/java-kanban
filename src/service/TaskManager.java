package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {

    int addTask(Task task);

    int addSubtask(Subtask subtask);

    int addEpic(Epic epic);

    List<Task> getTaskList();

    List<Subtask> getSubtaskList();

    List<Epic> getEpicList();

    List<Task> getPrioritizedTasks();

    void clearTask();

    void clearSubtask();

    void clearEpic();

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic1);

    void deleteTaskById(int id);

    void deleteSubtaskById(int id);

    void deleteEpicById(int id);

    List<Subtask> getSubtaskListByEpicId(int id);

    List<Task> getHistory();
}
