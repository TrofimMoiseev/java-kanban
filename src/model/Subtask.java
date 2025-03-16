package model;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String nameTask, String descriptionTask, TaskStatus status, Long minutes, LocalDateTime startTime, int epicId) {
        super(nameTask, descriptionTask, status, minutes, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{ nameTask='" + nameTask
                + "', descriptionTask ='" + descriptionTask
                + "', statusOfTask='" + statusOfTask
                + "', id=" + id
                + "', epicId=" + epicId
                + '}';
    }
}
