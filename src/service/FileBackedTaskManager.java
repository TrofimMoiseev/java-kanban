package service;

import model.*;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) throws ManagerSaveException {
        super(historyManager);
        this.file = file;
        if (file.isFile()) {
            loadFromFile(file);
        }
    }

    public void save() throws ManagerSaveException {//id,type,name,status,description,epic
        if (file == null || file.exists()) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл.");
        }
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epicId" + "\n");
            for (Task task : getTasksMap().values()) {
                writer.write(toString(task) + "\n");
            }
            for (Task task : getEpicsMap().values()) {
                writer.write(toString(task) + "\n");
            }
            for (Task task : getSubtasksMap().values()) {
                writer.write(toString(task) + "\n");
            }
            System.out.println("Данные успешно сохранены в файл.");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл.");
        }
    }

    private void loadFromFile(File file) throws ManagerSaveException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            fileReader.readLine();
            String line;
            while ((line = fileReader.readLine()) != null) {
                Task task = fromString(line);
                if (task instanceof Subtask) {
                    addSubtask((Subtask) task);
                } else if (task instanceof Epic) {
                    addEpic((Epic) task);
                } else {
                    addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных из файла.");
        }
    }

    private Task fromString(String value) { //id,type,name,status,description,epic
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
        TaskType type;
        if (getTasksMap().containsKey(task.getId())) {
            type = TaskType.TASK;
            return sb.append(task.getId()).append(",").append(type).append(",").append(task.getNameTask())
                    .append(",").append(task.getStatusOfTask()).append(",")
                    .append(task.getDescriptionTask()).toString();
        } else if (getSubtasksMap().containsKey(task.getId())) {
            type = TaskType.SUBTASK;
            int epicId = getSubtasksMap().get(task.getId()).getEpicId();
            return sb.append(task.getId()).append(",").append(type).append(",").append(task.getNameTask())
                    .append(",").append(task.getStatusOfTask()).append(",")
                    .append(task.getDescriptionTask()).append(", ").append(epicId).toString();
        } else if (getEpicsMap().containsKey(task.getId())) {
            type = TaskType.EPIC;
            return sb.append(task.getId()).append(",").append(type).append(",").append(task.getNameTask())
                    .append(",").append(task.getStatusOfTask()).append(",")
                    .append(task.getDescriptionTask()).toString();
        }
        return null;
    }

    @Override
    public int addTask(Task task) throws ManagerSaveException {
        super.addTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addSubtask(Subtask subtask) throws ManagerSaveException {
        super.addSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public int addEpic(Epic epic) throws ManagerSaveException {
        super.addEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public void clearTask() throws ManagerSaveException {
        super.clearTask();
        save();
    }

    @Override
    public void clearSubtask() throws ManagerSaveException {
        super.clearSubtask();
        save();
    }

    @Override
    public void clearEpic() throws ManagerSaveException {
        super.clearEpic();
        save();
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic1) throws ManagerSaveException {
        super.updateEpic(epic1);
        save();
    }

    @Override
    public void deleteTaskById(int id) throws ManagerSaveException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) throws ManagerSaveException {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) throws ManagerSaveException {
        super.deleteEpicById(id);
        save();
    }
}
