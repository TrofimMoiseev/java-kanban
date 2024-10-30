package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private int sequenceTask = 0;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private int getSequence() {  //Счетчик задач
        sequenceTask++;
        return sequenceTask;
    }

    public int addNewTask(Task task) { //Добавление задач в мапу
        task.setId(getSequence());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public int addNewSubtask(Subtask subtask) {
            if (epics.containsKey(subtask.getEpicId())) {
                subtask.setId(getSequence());
                subtasks.put(subtask.getId(), subtask);
                epics.get(subtask.getEpicId()).addNewSubtaskOnList(subtask);
                epics.get(subtask.getEpicId()).updateEpicStatus();
                return subtask.getId();
            }
        System.out.println("Ошибка, Epic для данного Subtask не найден.");
        return 0;
    }

    public int addNewEpic(Epic epic) {
        epic.setId(getSequence());
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public List<Task> getTaskList() {//Вывод мап
        return new ArrayList<>(tasks.values());
    }

    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtasks.values());
    }

    public List<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    public void clearTask() { //Очистка мап
        tasks.clear();

    }

    public void clearSubtask() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtaskFromList();
            epic.updateEpicStatus();
        }
    }

    public void clearEpic() {
        epics.clear();
        subtasks.clear();
    }

    public Task getTask(int id) { //Вывод задач по id
        return tasks.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);

    }

    public Epic getEpic(int id) {
        return epics.get(id);

    }

    public void updateTask(Task task) { //Обновление задач
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задача не найдена");
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).updateSubtaskList(subtask);
            epics.get(subtask.getEpicId()).updateEpicStatus();
        } else {
            System.out.println("Задача не найдена");
        }
    }

    public void updateEpic(Epic epic1) {
        if (epics.containsKey(epic1.getId())) {
            epics.get(epic1.getId()).setNameTask(epic1.getNameTask());
            epics.get(epic1.getId()).setDescriptionTask(epic1.getDescriptionTask());
        } else {
            System.out.println("Задача не найдена");
        }
    }

    public void deleteTaskById(int id) { //Удаление по id
        tasks.remove(id);

    }

    public void deleteSubtaskById(int id) { //Удаление субтасков из листа эпика и мапы
        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).getEpicId()).deleteSubtaskFromList(subtasks.get(id));
            epics.get(subtasks.get(id).getEpicId()).updateEpicStatus();
            subtasks.remove(id);

        } else {
            System.out.println("Задача не найдена");
        }
    }

    public void deleteEpicById(int id) { //Удаление эпиков, очистка субтасков эпика из листа и мапы
        if (epics.containsKey(id)) {
            for (Subtask subtask : epics.get(id).getSubtaskList()) {
                subtasks.remove(subtask.getId());
            }
            epics.remove(id);
        } else {
            System.out.println("Задача не найдена");
        }
    }

    public List<Subtask> getSubtaskListByEpicId(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id).getSubtaskList();
        } else {
            return List.of();
        }
    }


}