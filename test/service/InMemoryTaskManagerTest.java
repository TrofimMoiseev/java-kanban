package service;


import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        initTasks();
    }
}

