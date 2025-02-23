package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    public HashMap<Integer, Task> getTasksMap();

    public HashMap<Integer, Subtask> getSubtasksMap();

    public HashMap<Integer, Epic> getEpicsMap();

    int addTask(Task task) throws ManagerSaveException;

    int addSubtask(Subtask subtask) throws ManagerSaveException;

    int addEpic(Epic epic) throws ManagerSaveException;

    List<Task> getTaskList();

    List<Subtask> getSubtaskList();

    List<Epic> getEpicList();

    void clearTask() throws ManagerSaveException;

    void clearSubtask() throws ManagerSaveException;

    void clearEpic() throws ManagerSaveException;

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    void updateTask(Task task) throws ManagerSaveException;

    void updateSubtask(Subtask subtask) throws ManagerSaveException;

    void updateEpic(Epic epic1) throws ManagerSaveException;

    void deleteTaskById(int id) throws ManagerSaveException;

    void deleteSubtaskById(int id) throws ManagerSaveException;

    void deleteEpicById(int id) throws ManagerSaveException;

    List<Subtask> getSubtaskListByEpicId(int id);

    public List<Task> getHistory();
}
