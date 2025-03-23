package service;

import model.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    protected int sequenceTask = 0;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    protected int getSequence() {  //Счетчик задач
        sequenceTask++;
        return sequenceTask;
    }

    protected boolean timeValidation(Task task, Task newTask) {
        return task.getStartTime().isBefore(newTask.getEndTime()) && task.getEndTime().isAfter(newTask.getStartTime());
    }

    @Override
    public int addTask(Task task) {
        if (getPrioritizedTasks().stream().anyMatch(oldTask -> timeValidation(oldTask, task))) {
            throw new TaskValidationException("Задачи пересекаются");
        }
        task.setId(getSequence());
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        return task.getId();

    }

    @Override
    public int addSubtask(Subtask subtask) {
        if (getPrioritizedTasks().stream().anyMatch(oldTask -> timeValidation(oldTask, subtask))) {
            throw new TaskValidationException("Задачи пересекаются");
        } else if (!epics.containsKey(subtask.getEpicId())) {
            throw new NotFoundException("Эпик данной подзадачи не найден.");
        }
        subtask.setId(getSequence());
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.add(subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addNewSubtaskOnList(subtask);
        epic.updateEpicStatus();
        epic.updateEpicTime();
        return subtask.getId();
    }

    @Override
    public int addEpic(Epic epic) {
        epic.setId(getSequence());
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
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public void clearTask() { //Очистка мап
        tasks.values().forEach(task -> {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        });
        tasks.clear();
    }

    @Override
    public void clearSubtask() {
        subtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        });
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.clearSubtaskFromList();
            epic.updateEpicStatus();
            epic.updateEpicTime();
        });
    }

    @Override
    public void clearEpic() {
        epics.values().forEach(epic -> historyManager.remove(epic.getId()));
        subtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        });
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Задачи с таким id " + id + " не существует.");
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            throw new NotFoundException("Подзадачи с таким id " + id + " не существует.");
        }
        historyManager.add(subtask);
        return subtask;

    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NotFoundException("Эпика с таким id " + id + " не существует.");
        }
        historyManager.add(epic);
        return epic;

    }

    @Override
    public void updateTask(Task task) { //Обновление задач
        if (tasks.containsKey(task.getId()) && getPrioritizedTasks().stream()
                .filter(oldTask -> oldTask.getId() != (task.getId()))
                .noneMatch(oldTask -> timeValidation(oldTask, task))) {
            prioritizedTasks.remove(task);
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else {
            throw new TaskValidationException("Задачи пересекаются или ее не существуют");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId()) && getPrioritizedTasks().stream()
                .filter(oldTask -> oldTask.getId() != (subtask.getId()))
                .noneMatch(oldTask -> timeValidation(oldTask, subtask))) {
            prioritizedTasks.remove(subtask);
            subtasks.put(subtask.getId(), subtask);
            prioritizedTasks.add(subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.updateSubtaskList(subtask);
            epic.updateEpicStatus();
            epic.updateEpicTime();

        } else {
            throw new TaskValidationException("Задачи пересекаются или ее не существуют");
        }
    }

    @Override
    public void updateEpic(Epic epic1) {
        if (epics.containsKey(epic1.getId())) {
            epics.get(epic1.getId()).setNameTask(epic1.getNameTask());
            epics.get(epic1.getId()).setDescriptionTask(epic1.getDescriptionTask());
        } else {
            throw new NotFoundException("Задачи не существует.");
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
        } else {
            throw new NotFoundException("Задачи с таким id " + id + " не существует.");
        }
    }

    @Override
    public void deleteSubtaskById(int id) { //Удаление субтасков из листа эпика и мапы
        if (subtasks.containsKey(id)) {
            prioritizedTasks.remove(subtasks.get(id));
            Epic epic = epics.get(subtasks.get(id).getEpicId());
            epic.deleteSubtaskFromList(subtasks.get(id));
            epic.updateEpicStatus();
            epic.updateEpicTime();
            subtasks.remove(id);
            historyManager.remove(id);
        } else {
            throw new NotFoundException("Подзадачи с таким id " + id + " не существует.");
        }
    }

    @Override
    public void deleteEpicById(int id) { //Удаление эпиков, очистка субтасков эпика из листа и мапы
        if (epics.containsKey(id)) {
            for (Subtask subtask : epics.get(id).getSubtaskList()) {
                historyManager.remove(subtask.getId());
                prioritizedTasks.remove(subtask);
                subtasks.remove(subtask.getId());
            }
            historyManager.remove(id);
            epics.remove(id);
        } else {
            throw new NotFoundException("Эпика с таким id " + id + " не существует.");
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



