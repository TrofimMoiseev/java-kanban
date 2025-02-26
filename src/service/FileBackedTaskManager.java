package service;

import model.*;

import java.io.*;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private static final String HEADER = "id,type,name,status,description,epicId";

    public FileBackedTaskManager(HistoryManager historyManager, File file) throws ManagerSaveException {
        super(historyManager);
        this.file = file;
    }

    public void save() throws ManagerSaveException {

        try (Writer writer = new FileWriter(file)) {
            writer.write(HEADER + "\n");
            for (Task task : tasks.values()) {
                writer.write(toString(task) + "\n");
            }
            for (Task task : epics.values()) {
                writer.write(toString(task) + "\n");
            }
            for (Task task : subtasks.values()) {
                writer.write(toString(task) + "\n");
            }
            System.out.println("Данные успешно сохранены в файл.");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл.");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFile(file);
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            fileReader.readLine();
            String line;
            while ((line = fileReader.readLine()) != null) {
                Task task = fromString(line);
                if (task.getId() > fileBackedTaskManager.sequenceTask) {
                    fileBackedTaskManager.sequenceTask = task.getId();
                }
                if (task instanceof Subtask) {
                    fileBackedTaskManager.subtasks.put(task.getId(), (Subtask) task);
                    fileBackedTaskManager.epics.get(((Subtask) task).getEpicId()).addNewSubtaskOnList((Subtask) task);
                    fileBackedTaskManager.epics.get(((Subtask) task).getEpicId()).updateEpicStatus();
                } else if (task instanceof Epic) {
                    fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                } else {
                    fileBackedTaskManager.tasks.put(task.getId(), task);

                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных из файла.");
        }
        return fileBackedTaskManager;
    }

    private static Task fromString(String value) { //id,type,name,status,description,epic
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        TaskType taskType = TaskType.valueOf(parts[1]);
        String name = parts[2];
        TaskStatus status = TaskStatus.valueOf(parts[3]);
        String description = parts[4];
        int epicId = parts.length == 6 ? Integer.parseInt(parts[5]) : 0;

        Task task = switch (taskType) {
            case TASK -> new Task(name, description, status);
            case EPIC -> new Epic(name, description);
            case SUBTASK -> new Subtask(name, description, status, epicId);
        };
        task.setId(id);
        return task;
    }

    private String toString(Task task) { //id,type,name,status,description,epic
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",").append(task.getType()).append(",").append(task.getNameTask())
                .append(",").append(task.getStatusOfTask()).append(",")
                .append(task.getDescriptionTask());
        if (task.getType() == TaskType.SUBTASK) {
            int epicId = subtasks.get(task.getId()).getEpicId();
            sb.append(",").append(epicId);
        }
        return sb.toString();
    }

    @Override
    public int addTask(Task task) {
        super.addTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public int addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public void clearTask() {
        super.clearTask();
        save();
    }

    @Override
    public void clearSubtask() {
        super.clearSubtask();
        save();
    }

    @Override
    public void clearEpic() {
        super.clearEpic();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic1) {
        super.updateEpic(epic1);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }
}
