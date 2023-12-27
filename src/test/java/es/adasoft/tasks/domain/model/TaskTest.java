package es.adasoft.tasks.domain.model;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TaskTest {


    // Creating a new Task with a valid ID and name should set the ID and name fields correctly.
    @Test
    public void test_createTask_validIDAndName() {
        Long id = 1L;
        String name = "Task1";
        Task task = new Task(id, name);

        assertEquals(id, task.getId());
        assertEquals(name, task.getName());
    }

    // Two Tasks objects with the same ID and name should be considered equal.
    @Test
    public void test_taskEquality_sameIDAndName() {
        Long id = 1L;
        String name = "task1";
        Task task1 = new Task(id, name);
        Task task2 = new Task(id, name);

        assertEquals(task1, task2);
    }

    // The hashCode method should return the same value for two Tasks objects with the same ID and name.
    @Test
    public void test_hashCode_sameIDAndName() {
        Long id = 1L;
        String name = "task1";
        Task task1 = new Task(id, name);
        Task task2 = new Task(id, name);

        assertEquals(task1.hashCode(), task2.hashCode());
    }

}