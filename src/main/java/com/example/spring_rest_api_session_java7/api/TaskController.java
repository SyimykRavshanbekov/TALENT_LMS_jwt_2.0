package com.example.spring_rest_api_session_java7.api;

import com.example.spring_rest_api_session_java7.dto.task.TaskRequest;
import com.example.spring_rest_api_session_java7.dto.task.TaskResponse;
import com.example.spring_rest_api_session_java7.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/getAllByLessonId/{lessonId}")
    @PreAuthorize("isAuthenticated()")
    public List<TaskResponse> getAllTask(@PathVariable Long lessonId){
        return taskService.getAllTasks(lessonId);
    }

    @PostMapping("/save/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Instructor')")
    public TaskResponse saveTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) throws IOException {
        return taskService.addTask(id, taskRequest);
    }

    @GetMapping("/findById/{id}")
    @PreAuthorize("isAuthenticated()")
    public TaskResponse findById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Instructor')")
    public TaskResponse deleteById(@PathVariable Long id){
        return taskService.deleteTask(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Instructor')")
    public TaskResponse updateTask(@RequestBody TaskRequest taskRequest, @PathVariable Long id) throws IOException {
        return taskService.updateTask(taskRequest, id);
    }
}
