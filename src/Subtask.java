public class Subtask extends Task {
    Epic epic;

    public Subtask(String nameTask, String descriptionTask, TaskStatus status, Epic epic) {
        super(nameTask, descriptionTask, status);
        this.epic = epic;
        this.id = TaskManager.getSequenceSubtask();
    }
}
