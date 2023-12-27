package es.adasoft.tasks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.adasoft.tasks.domain.model.Task;
import es.adasoft.tasks.service.TaskService;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

public class ApplicationTest {

    @Test
    public void test_application_runs_without_errors() {
        Application.main(new String[]{});
    }
    @Test
    public void test_user_can_list_all_tasks() {
        // Mock TaskService
        TaskService taskService = mock(TaskService.class);

        // Create a list of tasks
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "Task 1"));
        tasks.add(new Task(2L, "Task 2"));

        // Set up the mock behavior
        when(taskService.getAllTasks()).thenReturn(tasks);

        // Create an instance of Application
        Application application = new Application(taskService);

        // Invoke the method to be tested
        Application.listAllTasks();

        // Verify the expected behavior
        verify(taskService).getAllTasks();
    }

    @Test
    public void test_user_can_view_task_by_id() {
        // Mock TaskService
        TaskService taskService = mock(TaskService.class);

        // Create a task
        Task task = new Task(1L, "Task 1");

        // Set up the mock behavior
        when(taskService.searchById(1L)).thenReturn(task);

        // Create an instance of Application
        Application application = new Application(taskService);

        // Invoke the method to be tested
        application.searchById();

        // Verify the expected behavior
        verify(taskService).searchById(1L);
    }

    @Test
    public void test_user_enters_invalid_choice() {
        // Mock Scanner
        Scanner scanner = mock(Scanner.class);

        // Set up the mock behavior
        when(scanner.nextInt()).thenReturn(6);

        // Create an instance of Application
        Application application = new Application(null);

        // Invoke the method to be tested
        application.runConsoleMenu();

        // Verify the expected behavior
        verify(scanner).nextInt();
    }
    @Test
    public void test_user_enters_non_existent_task_id() {
        // Mock TaskService
        TaskService taskService = mock(TaskService.class);

        // Set up the mock behavior
        when(taskService.searchById(1L)).thenReturn(null);

        // Create an instance of Application
        Application application = new Application(taskService);

        // Invoke the method to be tested
        application.searchById();

        // Verify the expected behavior
        verify(taskService).searchById(1L);
    }

    @Test
    public void test_user_tries_to_add_task_with_existing_id() {
        // Mock TaskService
        TaskService taskService = mock(TaskService.class);

        // Create a task
        Task task = new Task(1L, "Task 1");

        // Set up the mock behavior
        when(taskService.searchById(1L)).thenReturn(task);

        // Create an instance of Application
        Application application = new Application(taskService);

        // Invoke the method to be tested
        application.addNewTask();

        // Verify the expected behavior
        verify(taskService).searchById(1L);
    }
    @Test
    public void test_user_can_update_task() {
        // Arrange
        TaskService taskService = mock(TaskService.class);
        Application.setTaskService(taskService);
        Long id = 1L;
        String name = "Task 1";
        Task existingTask = new Task(id, name);
        when(taskService.searchById(id)).thenReturn(existingTask);

        // Act
        Application.updateTask(id, "Updated Task");

        // Assert
        verify(taskService).updateTask(existingTask);
    }
    @Test
    public void test_user_can_delete_task() {
        // Arrange
        TaskService taskService = mock(TaskService.class);
        Application.setTaskService(taskService);
        Long id = 1L;
        String name = "Task 1";
        Task existingTask = new Task(id, name);
        when(taskService.searchById(id)).thenReturn(existingTask);

        // Act
        Application.deleteTaskById(id);

        // Assert
        verify(taskService).deleteTask(id);
    }
}
