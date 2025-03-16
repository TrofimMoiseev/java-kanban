package model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String nameTask;
    protected String descriptionTask;
    protected TaskStatus statusOfTask;
    protected int id = 0;
    protected Duration duration;
    protected LocalDateTime startTime;


    public Task(String nameTask, String descriptionTask, TaskStatus statusOfTask, Long minutes, LocalDateTime startTime) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.statusOfTask = statusOfTask;
        this.duration = Duration.ofMinutes(minutes);
        this.startTime = startTime;
    }

    public Task(String nameTask, String descriptionTask , Long minutes, LocalDateTime startTime) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.statusOfTask = TaskStatus.NEW;
        this.duration = Duration.ofMinutes(minutes);
        this.startTime = startTime;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
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

    public void setId(int id) {
        this.id = id;
    }

    public TaskType getType() {
       return TaskType.TASK;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public  LocalDateTime getEndTime() {
        return this.startTime.plus(this.duration);
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
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}
