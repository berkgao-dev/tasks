package es.adasoft.tasks;

import es.adasoft.tasks.domain.model.Task;
import es.adasoft.tasks.service.TaskService;
import java.util.List;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Scanner scanner = new Scanner(System.in);

    private static TaskService taskService ;

    @Autowired
    public Application(TaskService taskService) {
        Application.taskService = taskService;
    }


    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

    }

    public static void setTaskService(TaskService taskService) {
        Application.taskService = taskService;
    }




    @Override
    public void run(String... args) {
        runConsoleMenu();
    }
    public static void runConsoleMenu() {
        String choice;

        do {
           menuTasks();

            choice = scanner.nextLine().trim();  // Consume the newline character

            switch (choice) {
                case "1" -> listAllTasks();
                case "2" -> listAllCompletedTasks();
                case "3" -> listAllPendingTasks();
                case "4" -> completeTask();
                case "5" -> changeTaskToPending();
                case "6" -> searchById();
                case "7" -> addNewTask();
                case "8" -> updateTask();
                case "9" -> deleteTask();
                case "0" -> System.out.println("Exiting the program. Goodbye!");
                default -> System.out.println("Invalid choice. Please enter a valid option.");
            }

            System.out.println();
        }
         while (!choice.equals("0"));

        // Close the scanner to avoid resource leaks
        scanner.close();
        System.exit(0);
    }

    private static void menuTasks() {
        System.out.println("Tasks Operations Menu:");
        System.out.println("1. List all tasks");
        System.out.println("2. List all completed tasks");
        System.out.println("3. List all pending tasks");
        System.out.println("4. Complete task");
        System.out.println("5. Change task to pending");
        System.out.println("6. View task by ID");
        System.out.println("7. Add new task");
        System.out.println("8. Update task");
        System.out.println("9. Delete task");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void changeTaskToPending() {
        try {

            System.out.print("Enter task ID: ");
            Long id = scanner.nextLong();
            scanner.nextLine();

            Task task = taskService.searchById(id);
            if (task != null) {
                taskService.changeTaskToPending(id);
                System.out.println("Task changed to pending successfully!");
            } else {
                System.out.println("No task found with ID " + id);
            }
        } catch (Exception e) {
            if (e.getMessage()==null) {
                System.out.println("Bad Sintaxis in Id, please use a valid number");
            } else {

                System.out.println("Error changing task to pending: " + e.getMessage());
            }
        }
    }
    private static void completeTask() {
        try {
            System.out.print("Enter task ID: ");
            Long id = scanner.nextLong();
            scanner.nextLine();

            Task task = taskService.searchById(id);
            if (task != null) {
                taskService.completeTask(id);
                System.out.println("Task completed successfully!");
            } else {
                System.out.println("No task found with ID " + id);
            }
        } catch (Exception e) {
            if (e.getMessage()==null) {
                System.out.println("Bad Sintaxis in Id, please use a valid number");
            } else {
                System.out.println("Error completing task: " + e.getMessage());
            }
        }
    }

    private static void listAllPendingTasks() {
        List<Task> tasks = taskService.getAllPendingTasks();
        tasks.forEach(System.out::println);
    }

    private static void listAllCompletedTasks() {
        List<Task> tasks = taskService.getAllCompletedTasks();
        tasks.forEach(System.out::println);
    }

    public static void deleteTaskById(Long id) {

        Task task = taskService.searchById(id);
        if(task != null) {
            taskService.deleteTask(id);
            System.out.println("Task deleted successfully!");
        } else {
            System.out.println("No task found with ID " + id);
        }
    }
    private static void deleteTask() {
        try {
            System.out.print("Enter task ID: ");
            Long id = scanner.nextLong();
            scanner.nextLine();
            deleteTaskById(id);
        } catch (Exception e) {
            if (e.getMessage()==null) {
                System.out.println("Bad Sintaxis in Id, please use a valid number");
            } else {
                System.out.println("Error deleting task: " + e.getMessage());
            }

        }
    }

    public static void updateTask(Long id, String updated_task) {
        try {
        Task task=new Task(id,updated_task);
        Task taskSearched = taskService.searchById(id);
        if(taskSearched != null) {
            taskService.updateTask(task);
            System.out.println("Task updated successfully!");
        } else {
            System.out.println("No task found with ID " + id);
        }
        }
        catch (Exception e) {
            if (e.getMessage()==null) {
                System.out.println("Bad Sintaxis in Id, please use a valid number");
            } else {
                System.out.println("Error updating task: " + e.getMessage());
            }
        }
    }
    private static void updateTask() {
        try {
        Task task = new Task();
        System.out.print("Enter task ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Enter new task name: ");
        String name = scanner.nextLine();
        task.setName(name);
        updateTask(id, name);
        }
        catch (Exception e) {
            if (e.getMessage()==null) {
                System.out.println("Bad Sintaxis in Id, please use a valid number");
            } else {
                System.out.println("Error updating task: " + e.getMessage());
            }
        }

    }

    static void addNewTask() {
        try {
            System.out.print("Enter task ID: ");
            Long id = scanner.nextLong();
            scanner.nextLine();

            System.out.print("Enter task name: ");
            Task task = taskService.searchById(id);
            if (task != null) {
                System.out.println("Task already exists!");
            } else {
                String name = scanner.nextLine();
                task = new Task(id, name);
                taskService.addTask(task);
                System.out.println("Task added successfully!");

            }
        } catch (Exception e) {
            if (e.getMessage()==null) {
                System.out.println("Bad Sintaxis in Id, please use a valid number");
            } else {
                System.out.println("Error adding task: " + e.getMessage());
            }
        }
    }

    public static void listAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        tasks.forEach(System.out::println);
    }

    static void searchById() {
        try{
        System.out.print("Enter task ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Task task = taskService.searchById(id);
        if(task != null) {
            System.out.println(task);
        } else {
            System.out.println("No task found with ID " + id);
        }
        }
        catch (Exception e) {
            if (e.getMessage()==null) {
                System.out.println("Bad Sintaxis in Id, please use a valid number");
            } else {
                System.out.println("Error searching task: " + e.getMessage());
            }
        }
    }

}