package es.adasoft.tasks.service;

import es.adasoft.tasks.domain.exception.TaskAlreadyExistsException;
import es.adasoft.tasks.domain.exception.TaskNotFoundException;
import es.adasoft.tasks.domain.model.Task;
import es.adasoft.tasks.repository.TaskRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    public void addTask(Task task) {
        Optional.ofNullable(task.getId())
                .orElseThrow(() -> new NullPointerException("Task ID cannot be null"));
        Optional.ofNullable(task.getName())
                .filter(name -> !name.trim().isEmpty())
                .orElseThrow(() -> new NullPointerException("Task name cannot be null"));
        taskRepository.findById(task.getId())
                .ifPresent(existingTask -> {
                    throw new TaskAlreadyExistsException("Task with ID " + task.getId() + " already exists");
                });
        taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    public List<Task> getAllCompletedTasks() {
        return taskRepository.findByCompletedOrderByEndDateDesc(true);
    }

    public List<Task> getAllPendingTasks() {
        return taskRepository.findByCompletedOrderByStartDateAsc(false);
    }
    public List<Task> searchTasksByName(String partialName) {
        return taskRepository.findByNameContaining(partialName);
    }

    public Task updateTask(Task task) {
        return taskRepository.findById(task.getId())
                .map(existingTask -> {
                    existingTask.setName(task.getName());
                    existingTask.setCompleted(task.getCompleted());
                    existingTask.setStartDate(task.getStartDate());
                    if(task.getCompleted()) {
                        existingTask.setEndDate(task.getEndDate());
                    }
                    else {
                        existingTask.setEndDate(null);
                    }
                    return taskRepository.save(existingTask);
                })
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + task.getId() + " not found"));
    }
    public void deleteTask(Long id) {
        Task taskToDelete = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        taskRepository.delete(taskToDelete);
    }

    public Task searchById(Long id) {
        return  taskRepository.findById(id).orElse(null);
    }

    public Task completeTask(Long id) {
        Task taskToComplete = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        if(taskToComplete.getCompleted()) {
            throw new TaskAlreadyExistsException("Task with ID " + id + " already completed");
        }
        taskToComplete.setCompleted(true);
        taskToComplete.setEndDate(new Date());
        return taskRepository.save(taskToComplete);
    }
    public Task changeTaskToPending(Long id) {
        Task taskToComplete = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        if(!taskToComplete.getCompleted()) {
            throw new TaskAlreadyExistsException("Task with ID " + id + " already pending");
        }
        taskToComplete.setCompleted(false);
        taskToComplete.setEndDate(null);
        return taskRepository.save(taskToComplete);
    }
}
