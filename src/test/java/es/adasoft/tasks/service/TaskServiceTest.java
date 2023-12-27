package es.adasoft.tasks.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.adasoft.tasks.domain.exception.TaskAlreadyExistsException;
import es.adasoft.tasks.domain.exception.TaskNotFoundException;
import es.adasoft.tasks.domain.model.Task;
import es.adasoft.tasks.repository.TaskRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TaskServiceTest {
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskServiceMock;

    @BeforeEach
    public void setUp() {
        taskRepository = mock(TaskRepository.class);
        MockitoAnnotations.openMocks(this);
    }

    // Adding a new Task with a unique ID should save the Task to the repository
    @Test
    public void test_addTask_uniqueID() {

        Task task = new Task(1L, "Task1");

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        when(taskRepository.save(task)).thenReturn(task);

        taskServiceMock.addTask(task);

        Mockito.verify(taskRepository, times(1)).findById(1L);
        Mockito.verify(taskRepository, times(1)).save(task);
    }

    // Adding a new Task with a non-unique ID should throw a TaskAlreadyExistsException
    @Test
    public void test_addTask_nonUniqueID() {
        Task task = new Task(1L, "task1");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(TaskAlreadyExistsException.class, () -> taskServiceMock.addTask(task));

        Mockito.verify(taskRepository, times(1)).findById(1L);
        Mockito.verify(taskRepository, never()).save(task);
    }

    // Adding a new Task with null ID should throw a NullPointerException
    @Test
    public void test_addTask_nullID() {
        Task task = new Task(null, "Task1");

        assertThrows(NullPointerException.class, () -> taskServiceMock.addTask(task));

        Mockito.verify(taskRepository, never()).findById(Mockito.anyLong());
        Mockito.verify(taskRepository, never()).save(any(Task.class));
    }

    // Adding a new Task with null name should throw a NullPointerException
    @Test
    public void test_addTask_nullName() {
        Task task = new Task(1L, null);

        assertThrows(NullPointerException.class, () -> taskServiceMock.addTask(task));

        Mockito.verify(taskRepository, never()).findById(Mockito.anyLong());
        Mockito.verify(taskRepository, never()).save(any(Task.class));
    }

    // Adding a new Task with empty name should throw an IllegalArgumentException
    @Test
    public void test_addTask_emptyName() {
        Task task = new Task(1L, "");

        assertThrows(NullPointerException.class, () -> taskServiceMock.addTask(task));

        Mockito.verify(taskRepository, never()).findById(Mockito.anyLong());
        Mockito.verify(taskRepository, never()).save(any(Task.class));
    }

    // Adding a new Task with ID that already exists in the repository should throw a TaskAlreadyExistsException
    @Test
    public void test_addTask_existingID() {
        Task existingTask = new Task(1L, "task1");
        Task newTask = new Task(1L, "task2");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        assertThrows(TaskAlreadyExistsException.class, () -> taskServiceMock.addTask(newTask));

        Mockito.verify(taskRepository, times(1)).findById(1L);
        Mockito.verify(taskRepository, never()).save(any(Task.class));
    }
    @Test
    public void test_getAllTaskes_emptyList() {

        when(taskRepository.findAll()).thenReturn(Collections.emptyList());
        List<Task> result = taskServiceMock.getAllTasks();
        assertTrue(result.isEmpty());

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void test_getAllTaskes_nonEmptyList() {

        Task task1 = new Task(1L, "Task1");
        Task task2 = new Task(2L, "Task2");
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));
        List<Task> result = taskServiceMock.getAllTasks();
        assertEquals(2, result.size());
        assertEquals(task1, result.get(0));
        assertEquals(task2, result.get(1));

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void test_completed_tasks_sorted_by_end_date() {
        // Arrange
        List<Task> completedTasks = new ArrayList<>();
        completedTasks.add(new Task(1L, "Task 1", true, new Date(), new Date()));
        completedTasks.add(new Task(2L, "Task 2", true, new Date(), new Date()));
        completedTasks.add(new Task(3L, "Task 3", true, new Date(), new Date()));
        Mockito.when(taskRepository.findByCompletedOrderByEndDateDesc(true)).thenReturn(completedTasks);

        // Act
        List<Task> result = taskServiceMock.getAllCompletedTasks();

        // Assert
        assertEquals(completedTasks, result);
    }

    @Test
    public void test_no_completed_tasks() {
        // Arrange
        List<Task> completedTasks = new ArrayList<>();
        Mockito.when(taskRepository.findByCompletedOrderByEndDateDesc(true)).thenReturn(completedTasks);

        // Act
        List<Task> result = taskServiceMock.getAllCompletedTasks();

        // Assert
        assertTrue(result.isEmpty());
    }
    @Test
    public void test_all_tasks_pending() {
        // Arrange
        List<Task> completedTasks = new ArrayList<>();
        Mockito.when(taskRepository.findByCompletedOrderByEndDateDesc(true)).thenReturn(completedTasks);

        // Act
        List<Task> result = taskServiceMock.getAllCompletedTasks();

        // Assert
        assertTrue(result.isEmpty());
    }
    @Test
    public void test_completed_tasks_without_end_date() {
        // Arrange
        List<Task> completedTasks = new ArrayList<>();
        completedTasks.add(new Task(1L, "Task 1", true, new Date(), null));
        completedTasks.add(new Task(2L, "Task 2", true, new Date(), null));
        completedTasks.add(new Task(3L, "Task 3", true, new Date(), null));
        Mockito.when(taskRepository.findByCompletedOrderByEndDateDesc(true)).thenReturn(completedTasks);

        // Act
        List<Task> result = taskServiceMock.getAllCompletedTasks();

        // Assert
        assertTrue(result.isEmpty());
    }
    @Test
    public void test_no_tasks_in_repository() {
        // Arrange
        List<Task> completedTasks = new ArrayList<>();
        Mockito.when(taskRepository.findByCompletedOrderByEndDateDesc(true)).thenReturn(completedTasks);

        // Act
        List<Task> result = taskServiceMock.getAllCompletedTasks();

        // Assert
        assertTrue(result.isEmpty());
    }
    @Test
    public void test_multiple_completed_tasks_with_different_end_dates() {
        // Arrange
        List<Task> completedTasks = new ArrayList<>();
        completedTasks.add(new Task(1L, "Task 1", true, new Date(), new Date()));
        completedTasks.add(new Task(2L, "Task 2", true, new Date(), new Date()));
        completedTasks.add(new Task(3L, "Task 3", true, new Date(), new Date()));
        Mockito.when(taskRepository.findByCompletedOrderByEndDateDesc(true)).thenReturn(completedTasks);

        // Act
        List<Task> result = taskServiceMock.getAllCompletedTasks();

        // Assert
        assertEquals(completedTasks, result);
    }
    @Test
    public void test_return_empty_list_if_no_pending_tasks() {
        // Arrange
        List<Task> expectedTasks = new ArrayList<>();

        when(taskRepository.findByCompletedOrderByStartDateAsc(false)).thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = taskServiceMock.getAllPendingTasks();

        // Assert
        assertEquals(expectedTasks, actualTasks);
    }
    @Test
    public void test_return_empty_list_if_all_tasks_completed() {
        // Arrange
        List<Task> expectedTasks = new ArrayList<>();
        Task task1 = new Task(1L, "Task 1");
        task1.setCompleted(true);
        task1.setStartDate(new Date());
        expectedTasks.add(task1);
        Task task2 = new Task(2L, "Task 2");
        task2.setCompleted(true);
        task2.setStartDate(new Date());
        expectedTasks.add(task2);
        Task task3 = new Task(3L, "Task 3");
        task3.setCompleted(true);
        task3.setStartDate(new Date());
        expectedTasks.add(task3);

        when(taskRepository.findByCompletedOrderByStartDateAsc(false)).thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = taskServiceMock.getAllPendingTasks();

        // Assert
        assertEquals(expectedTasks, actualTasks);
    }
    @Test
    public void test_return_empty_list_if_no_tasks_in_repository() {
        // Arrange
        List<Task> expectedTasks = new ArrayList<>();

        when(taskRepository.findByCompletedOrderByStartDateAsc(false)).thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = taskServiceMock.getAllPendingTasks();

        // Assert
        assertEquals(expectedTasks, actualTasks);
    }
    @Test
    public void test_return_tasks_with_null_start_date_if_not_completed() {
        // Arrange
        List<Task> expectedTasks = new ArrayList<>();
        Task task1 = new Task(1L, "Task 1");
        task1.setCompleted(false);
        expectedTasks.add(task1);
        Task task2 = new Task(2L, "Task 2");
        task2.setCompleted(false);
        expectedTasks.add(task2);
        Task task3 = new Task(3L, "Task 3");
        task3.setCompleted(false);
        expectedTasks.add(task3);

        when(taskRepository.findByCompletedOrderByStartDateAsc(false)).thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = taskServiceMock.getAllPendingTasks();

        // Assert
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    public void test_return_tasks_with_null_start_date_if_completed() {
        // Arrange
        List<Task> expectedTasks = new ArrayList<>();
        Task task1 = new Task(1L, "Task 1");
        task1.setCompleted(true);
        expectedTasks.add(task1);
        Task task2 = new Task(2L, "Task 2");
        task2.setCompleted(true);
        expectedTasks.add(task2);
        Task task3 = new Task(3L, "Task 3");
        task3.setCompleted(true);
        expectedTasks.add(task3);

        when(taskRepository.findByCompletedOrderByStartDateAsc(false)).thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = taskServiceMock.getAllPendingTasks();

        // Assert
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    public void test_changeTaskToPending_completedTask() {
        // Arrange
        Long taskId = 1L;
        Task completedTask = new Task(taskId, "Task 1");
        completedTask.setCompleted(true);
        completedTask.setEndDate(new Date());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(completedTask));

        // Act
        Task result = taskServiceMock.changeTaskToPending(taskId);

        // Assert
        assertFalse(result.getCompleted());
        assertNull(result.getEndDate());
        verify(taskRepository, times(1)).save(completedTask);
    }
    @Test
    public void test_changeTaskToPending_pendingTask() {
        // Arrange
        Long taskId = 1L;
        Task pendingTask = new Task(taskId, "Task 1");
        pendingTask.setCompleted(false);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(pendingTask));

        // Act
        Task result = taskServiceMock.changeTaskToPending(taskId);

        // Assert
        assertEquals(pendingTask, result);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void test_changeTaskToPending_saveChanges() {
        // Arrange
        Long taskId = 1L;
        Task completedTask = new Task(taskId, "Task 1");
        completedTask.setCompleted(true);
        completedTask.setEndDate(new Date());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(completedTask));

        // Act
        Task result = taskServiceMock.changeTaskToPending(taskId);

        // Assert
        assertFalse(result.getCompleted());
        assertNull(result.getEndDate());
        verify(taskRepository, times(1)).save(completedTask);
    }
    @Test
    public void test_changeTaskToPending_invalidId() {
        // Arrange
        Long taskId = null;

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskServiceMock.changeTaskToPending(taskId));
        verify(taskRepository, never()).findById(any(Long.class));
    }
    @Test
    public void test_changeTaskToPending_nonExistentTask() {
        // Arrange
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskServiceMock.changeTaskToPending(taskId));
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    public void test_changeTaskToPending_nullCompletionStatus() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task(taskId, "Task 1");
        task.setCompleted(null);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> taskServiceMock.changeTaskToPending(taskId));
        verify(taskRepository, never()).save(any(Task.class));
    }
}