import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Subtask> subtaskList = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
        this.id = TaskManager.getSequenceEpic();
        this.statusOfTask = TaskStatus.NEW;

    }

    public void addSubtaskOnList(Subtask subtask) {
        subtaskList.add(subtask);
    }


    @Override
    public TaskStatus getStatusOfTask() {
        int countDone = 0;
        int countNew = 0;
        for (Subtask subtask : subtaskList) {
            if (subtask.statusOfTask == TaskStatus.DONE) {
                countDone++;
            } else if (subtask.statusOfTask == TaskStatus.NEW) {
                countNew++;
            }
        }
        if (countDone == subtaskList.size()) {
            return TaskStatus.DONE;
        } else if (countNew == subtaskList.size() || subtaskList.isEmpty()) {
            return TaskStatus.NEW;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }
}
