public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        // Создание

        Task task1 = new Task("Task #1", "Task1 description");
        manager.addNewTask(task1);
        Task task2 = new Task("Task #2", "Task2 description", TaskStatus.IN_PROGRESS);
        manager.addNewTask(task2);

        Epic epic1 = new Epic("Epic #1", "Epic1 description");
        manager.addNewEpic(epic1);
        Epic epic2 = new Epic("Epic #2", "Epic2 description");
        manager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", TaskStatus.NEW, epic1);
        manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask #2-1", "Subtask2 description", TaskStatus.NEW, epic1);
        manager.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask #1-2", "Subtask1 description", TaskStatus.NEW, epic2);
        manager.addNewSubtask(subtask3);

        manager.getTasks();
        manager.getEpics();
        manager.getSubtasks();


        // Обновление

        task1.setStatusOfTask(TaskStatus.IN_PROGRESS);
        manager.updateTask(task1.id, task1);
        System.out.println("CHANGE STATUS: task1 NEW->IN_PROGRESS");
        task2.setStatusOfTask(TaskStatus.DONE);
        manager.updateTask(task2.id, task2);
        System.out.println("CHANGE STATUS: task2 IN_PROGRESS->DONE");

        System.out.println("Задачи:");
        manager.getTasks();


        subtask1.setStatusOfTask(TaskStatus.IN_PROGRESS);
        System.out.println("CHANGE STATUS: subtask1 NEW->IN_PROGRESS");
        manager.updateSubtask(subtask1.id, subtask1);

        subtask2.setStatusOfTask(TaskStatus.DONE);
        System.out.println("CHANGE STATUS: subtask2 NEW->DONE");
        manager.updateSubtask(subtask2.id, subtask2);

        subtask3.setStatusOfTask(TaskStatus.DONE);
        System.out.println("CHANGE STATUS: subtask3 NEW->DONE");
        manager.updateSubtask(subtask3.id, subtask3);

        System.out.println("Подзадачи:");

        manager.getSubtasks();


        System.out.println("Эпики:");

        manager.getEpics();




        // Удаление

        System.out.println("DELETE: task1");

        manager.deleteTaskById(task1.id);

        System.out.println("DELETE: epic1");

        manager.deleteEpicById(epic1.id);

        manager.getTasks();
        manager.getEpics();
        manager.getSubtasks();

    }
}
