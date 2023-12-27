package es.adasoft.tasks.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.adasoft.tasks.domain.model.Task;
import es.adasoft.tasks.service.TaskService;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class TasksRestControllerTest {
    @Autowired
    private TasksRestController tasksRestController;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(tasksRestController)
                .build();
    }
    @TestConfiguration
    protected static class Config {
        @Bean
        public TaskService taskService() {
            return Mockito.mock(TaskService.class);
        }

    }


    @Test
    void addTask() throws Exception {
        long taskId = 100L;
        Task task = new Task(taskId,"TestTask");
        mockMvc.perform(post("/tasks/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());

    }

    @Test
    void getAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Collections.<Task>emptyList());

        mockMvc.perform(get("/tasks/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void searchById() throws Exception {
        Long taskId = 1L;
        Task task = new Task(taskId,"TestTask");
        when(taskService.searchById(taskId)).thenReturn(task);

        mockMvc.perform(get("/tasks/findById?id=" + taskId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskId));

       verify(taskService, times(1)).searchById(taskId);
    }

    @Test
    void searchTaskByName() throws Exception {
        String partialName = "Test";
        when(taskService.searchTasksByName(partialName)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tasks/search?partialName=" + partialName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(taskService, times(1)).searchTasksByName(partialName);
    }

    @Test
    void modifyTask() throws Exception {
        Long taskId=1L;
        Task task = new Task(taskId,"TestTask");
        mockMvc.perform(post("/tasks/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());
        mockMvc.perform(put("/tasks/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTask() throws Exception {
        Long taskId = 1000L;
        Task task = new Task(taskId,"TestTask");
        mockMvc.perform(post("/tasks/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/tasks/delete?id=" + taskId))
                .andExpect(status().isOk());

    }
    @Test
    void getAllCompletedTasks() throws Exception {
        when(taskService.getAllCompletedTasks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tasks/getAllCompleted"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());

        verify(taskService, times(1)).getAllCompletedTasks();
    }

    @Test
    void getAllPendingTasks() throws Exception {
        when(taskService.getAllPendingTasks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tasks/getAllPending"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());

        verify(taskService, times(1)).getAllPendingTasks();
    }

    @Test
    void completeTask() throws Exception {
        Long taskId = 1L;
        Task task = new Task(taskId, "TestTask");
        when(taskService.completeTask(taskId)).thenReturn(task);

        mockMvc.perform(put("/tasks/complete?id=" + taskId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(taskId));

        verify(taskService, times(1)).completeTask(taskId);
    }

    @Test
    void pendingTask() throws Exception {
        Long taskId = 1L;
        Task task = new Task(taskId, "TestTask");
        when(taskService.changeTaskToPending(taskId)).thenReturn(task);

        mockMvc.perform(put("/tasks/pending?id=" + taskId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(taskId));

        verify(taskService, times(1)).changeTaskToPending(taskId);
    }
}
