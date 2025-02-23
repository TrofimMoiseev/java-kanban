package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private int sequenceTask = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private int getSequence() {  //Счетчик задач
        sequenceTask++;
        return sequenceTask;
    }

    private void setSequence(int newSequenceTask) {
        if (newSequenceTask > sequenceTask) {
            sequenceTask = newSequenceTask;
        }
    }


    @Override
    public HashMap<Integer, Task> getTasksMap() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasksMap() {
        return subtasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpicsMap() {
        return epics;
    }

    @Override
    public int addTask(Task task) throws ManagerSaveException { //Добавление задач в мапу
        if (task.getId() == 0) {
            task.setId(getSequence());
        } else {
            setSequence(task.getId());
        }
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int addSubtask(Subtask subtask) throws ManagerSaveException {

        if (epics.containsKey(subtask.getEpicId())) {
            if (subtask.getId() == 0) {
                subtask.setId(getSequence());
            } else {
                setSequence(subtask.getId());
            }
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).addNewSubtaskOnList(subtask);
            epics.get(subtask.getEpicId()).updateEpicStatus();
            return subtask.getId();
        }
        System.out.println("Ошибка, Epic для данного Subtask не найден.");
        return 0;
    }

    @Override
    public int addEpic(Epic epic) throws ManagerSaveException {
        if (epic.getId() == 0) {
            epic.setId(getSequence());
        } else {
            setSequence(epic.getId());
        }
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public List<Task> getTaskList() { //Вывод мап
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void clearTask() throws ManagerSaveException { //Очистка мап
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void clearSubtask() throws ManagerSaveException {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtaskFromList();
            epic.updateEpicStatus();
        }
    }

    @Override
    public void clearEpic() throws ManagerSaveException {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task getTask(int id) { //Вывод задач по id
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);

    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);

    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException { //Обновление задач
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задача не найдена");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).updateSubtaskList(subtask);
            epics.get(subtask.getEpicId()).updateEpicStatus();
        } else {
            System.out.println("Задача не найдена");
        }
    }

    @Override
    public void updateEpic(Epic epic1) throws ManagerSaveException {
        if (epics.containsKey(epic1.getId())) {
            epics.get(epic1.getId()).setNameTask(epic1.getNameTask());
            epics.get(epic1.getId()).setDescriptionTask(epic1.getDescriptionTask());
        } else {
            System.out.println("Задача не найдена");
        }
    }

    @Override
    public void deleteTaskById(int id) throws ManagerSaveException { //Удаление по id
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) throws ManagerSaveException { //Удаление субтасков из листа эпика и мапы
        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).getEpicId()).deleteSubtaskFromList(subtasks.get(id));
            epics.get(subtasks.get(id).getEpicId()).updateEpicStatus();
            subtasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Задача не найдена");
        }
    }

    @Override
    public void deleteEpicById(int id) throws ManagerSaveException { //Удаление эпиков, очистка субтасков эпика из листа и мапы
        if (epics.containsKey(id)) {
            for (Subtask subtask : epics.get(id).getSubtaskList()) {
                historyManager.remove(subtask.getId());
                subtasks.remove(subtask.getId());
            }
            historyManager.remove(id);
            epics.remove(id);
        } else {
            System.out.println("Задача не найдена");
        }
    }

    @Override
    public List<Subtask> getSubtaskListByEpicId(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id).getSubtaskList();
        } else {
            return List.of();
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}

