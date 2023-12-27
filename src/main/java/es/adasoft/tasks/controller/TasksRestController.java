package es.adasoft.tasks.controller;

import es.adasoft.tasks.domain.model.Task;
import es.adasoft.tasks.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public final class TasksRestController {
    @Autowired
    private final TaskService taskService;

    @Autowired
    public TasksRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    public void addTask(@RequestBody Task task) {
        taskService.addTask(task);
    }

    @GetMapping("/getAll")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/getAllCompleted")
    public List<Task> getAllCompletedTasks() {
        return taskService.getAllCompletedTasks();
    }

    @GetMapping("/getAllPending")
    public List<Task> getAllPendingTasks() {
        return taskService.getAllPendingTasks();
    }

    @GetMapping("/findById")
    public Task searchById(@RequestParam Long id) {
        return taskService.searchById(id);
    }

    @GetMapping("/search")
    public List<Task> searchTasksByName(@RequestParam String partialName) {
        return taskService.searchTasksByName(partialName);
    }

    @PutMapping("/modify")
    public Task modifyTask(@RequestBody Task task) {
        return taskService.updateTask(task);
    }

    @PutMapping("/complete")
    public Task completeTask(@RequestParam Long id) {
        return taskService.completeTask(id);
    }

    @PutMapping("/pending")
    public Task pendingTask(@RequestParam Long id) {
        return taskService.changeTaskToPending(id);
    }

    @DeleteMapping("/delete")
    public void deleteTask(@RequestParam Long id) {
        taskService.deleteTask(id);
    }

}
