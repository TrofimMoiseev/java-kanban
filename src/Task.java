import java.util.Objects;

public class Task {
    protected String nameTask;
    protected String descriptionTask;
    protected TaskStatus statusOfTask;
    protected int id = 0;

    public Task(String nameTask, String descriptionTask, TaskStatus statusOfTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.statusOfTask = statusOfTask;
        this.id = TaskManager.getSequenceTask();
    }

    public Task(String nameTask, String descriptionTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.statusOfTask = TaskStatus.NEW;
        this.id = TaskManager.getSequenceTask();
    }

    public String getNameTask() {
        return nameTask;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public TaskStatus getStatusOfTask() {
        return statusOfTask;
    }

    public void setStatusOfTask(TaskStatus statusOfTask) {
        this.statusOfTask = statusOfTask;
    }

    public int getId() {
        return id;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{ nameTask='" + nameTask
                + "', descriptionTask ='" + descriptionTask
                + "', statusOfTask='" + statusOfTask
                + "', id=" + id
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(nameTask, task.nameTask) && Objects.equals(descriptionTask, task.descriptionTask) && statusOfTask == task.statusOfTask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTask, descriptionTask, statusOfTask, id);
    }
}
