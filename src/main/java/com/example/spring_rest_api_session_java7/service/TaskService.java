package com.example.spring_rest_api_session_java7.service;

import com.example.spring_rest_api_session_java7.dto.task.TaskRequest;
import com.example.spring_rest_api_session_java7.dto.task.TaskResponse;

import java.util.List;

public interface TaskService {
    List<TaskResponse> getAllTasks(Long id);

    TaskResponse addTask(Long id, TaskRequest taskRequest);

    TaskResponse getTaskById(Long id);

    TaskResponse updateTask(TaskRequest taskRequest, Long id);

    TaskResponse deleteTask(Long id);
}
