import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int sequenceTask = 0;
    private static int sequenceEpic = 0;
    private static int sequenceSubtask = 0;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    public static int getSequenceTask() {  //Счетчик задач
        sequenceTask++;
        return sequenceTask;
    }

    public static int getSequenceEpic() {
        sequenceEpic++;
        return sequenceEpic;
    }

    public static int getSequenceSubtask() {
        sequenceSubtask++;
        return sequenceSubtask;
    }

    public void addNewTask(Task task) { //Добавление задач в мапу
        tasks.put(sequenceTask, task);
    }

    public void addNewSubtask(Subtask subtask) {
        subtasks.put(sequenceSubtask, subtask);
        subtask.epic.addSubtaskOnList(subtask);
    }

    public void addNewEpic(Epic epic) {
        epics.put(sequenceEpic, epic);
    }

    public void getTasks() {//Вывод мап
        for (int k : tasks.keySet()) {
            System.out.println("Id = " + k + ", Задача = " + tasks.get(k) + ".");
        }
    }

    public void getSubtasks() {
        for (int k : subtasks.keySet()) {
            System.out.println("Id = " + k + ", Задача = " + subtasks.get(k) + ".");
        }
    }

    public void getEpics() {
        for (int k : epics.keySet()) {
            System.out.println("Id = " + k + ", Задача = " + epics.get(k) + ".");
        }
    }

    public void deleteTask() { //Очистка мап
        tasks.clear();

    }

    public void deleteSubtask() {
        subtasks.clear();
        for (Epic epic : epics.values())
            epic.subtaskList.clear();
    }

    public void deleteEpic() {
        for (Epic epic : epics.values())
            epic.subtaskList.clear();
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

    public void updateTask(int id, Task task) { //Обновление задач
        tasks.put(id, task);
    }

    public void updateSubtask(int id, Subtask subtask) {
        subtasks.put(id, subtask);
        subtask.epic.setStatusOfTask(subtask.epic.getStatusOfTask());
    }

    public void updateEpic(int id, Epic epic) {
        epics.put(id, epic);
    }

    public void deleteTaskById(int id) { //Удаление по id
        tasks.remove(id);
    }

    public void deleteSubtaskById(int id) { //Удаление субтасков из листа эпика и мапы
        subtasks.get(id).epic.subtaskList.remove(subtasks.get(id));
        subtasks.remove(id);
    }

    public void deleteEpicById(int id) { //Удаление эпиков, очистка субтасков эпика из листа и мапы
        for (Subtask subtask : epics.get(id).subtaskList) {
            subtasks.remove(subtask.id);
        }
        epics.get(id).subtaskList.clear();
        epics.remove(id);
    }

    public ArrayList<Subtask> getSubtaskListByEpic(Epic epic) {
        return epic.subtaskList;
    }


}