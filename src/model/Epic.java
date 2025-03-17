package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class Epic extends Task {
    private final ArrayList<Subtask> subtaskList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask, null, null);
        this.statusOfTask = TaskStatus.NEW;
    }

    public void addNewSubtaskOnList(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public void deleteSubtaskFromList(Subtask subtask) {
        subtaskList.remove(subtask);
    }

    public void clearSubtaskFromList() {
        subtaskList.clear();
    }

    public void updateSubtaskList(Subtask subtask) {
        subtaskList.remove(subtask);
        subtaskList.add(subtask);
    }

    public void updateEpicStatus() {
        int countDone = 0;
        int countNew = 0;
        if (subtaskList.isEmpty()) {
            setStatusOfTask(TaskStatus.NEW);
        } else {
            for (Subtask subtask : subtaskList) {
                if (subtask.statusOfTask == TaskStatus.DONE) {
                    countDone++;
                } else if (subtask.statusOfTask == TaskStatus.NEW) {
                    countNew++;
                }
            }
            if (countNew == subtaskList.size()) {
                setStatusOfTask(TaskStatus.NEW);
            } else if (countDone == subtaskList.size()) {
                setStatusOfTask(TaskStatus.DONE);
            } else {
                setStatusOfTask(TaskStatus.IN_PROGRESS);
            }
        }
    }

    private void updateEpicDuration() {
        long durationSum = 0L;
        if (subtaskList.isEmpty()) {
            setDuration(Duration.ofMinutes(durationSum));
        } else {
            for (Subtask subtask : subtaskList) {
                durationSum = durationSum + subtask.duration.toMinutes();
            }
            setDuration(Duration.ofMinutes(durationSum));
        }
    }

    private void updateEpicStartTime() {
        Optional<Subtask> startTimeSubtask = subtaskList.stream().min(Comparator.comparing(Subtask::getStartTime));
        startTimeSubtask.ifPresent(subtask -> setStartTime(subtask.getStartTime()));
    }

    private void updateEpicEndTime() {

        Optional<Subtask> endTimeSubtask = subtaskList.stream().max(Comparator.comparing(Subtask::getEndTime));
        endTimeSubtask.ifPresent(subtask -> setEndTime(subtask.getEndTime()));
    }

    public void updateEpicTime() {
        updateEpicStartTime();
        updateEpicDuration();
        updateEpicEndTime();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public ArrayList<Subtask> getSubtaskList() {
        return subtaskList;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{ nameTask='" + nameTask
                + "', descriptionTask ='" + descriptionTask
                + "', statusOfTask='" + statusOfTask
                + "', id=" + id
                + "', subtaskList.size=" + subtaskList.size()
                + '}';
    }
}
