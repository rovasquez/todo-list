package com.example.todo.rest;

import com.example.todo.model.Task;
import com.example.todo.model.TaskStatus;
import com.example.todo.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/tasks")
public class TaskController {

    private TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity createTask(@RequestBody TaskRequest taskRequest) {
        Task task = new Task(taskRequest);

        repository.save(task);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/statuses/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(repository.findByStatus(status));
    }

    @GetMapping(value = "/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        return ResponseEntity.of(repository.findById(taskId));
    }

    @PatchMapping(value = "/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody TaskRequest taskRequest) {
        Task task = repository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id = " + taskId));
        task.setContent(taskRequest.getContent());
        task.setStatus(taskRequest.getTaskStatus());

        return ResponseEntity.ok(repository.save(task));
    }

    @DeleteMapping(value = "/{taskId}")
    public ResponseEntity updateTask(@PathVariable Long taskId) {
        Task task = repository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id = " + taskId));
        repository.delete(task);
        return new ResponseEntity(HttpStatus.OK);
    }


    public static class TaskRequest {
        private String content;

        private TaskStatus taskStatus;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public TaskStatus getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(TaskStatus taskStatus) {
            this.taskStatus = taskStatus;
        }
    }
}
