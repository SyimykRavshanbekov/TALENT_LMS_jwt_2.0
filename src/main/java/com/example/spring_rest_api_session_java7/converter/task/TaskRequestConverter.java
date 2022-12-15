package com.example.spring_rest_api_session_java7.converter.task;

import com.example.spring_rest_api_session_java7.dto.task.TaskRequest;
import com.example.spring_rest_api_session_java7.entities.Task;
import org.springframework.stereotype.Component;


@Component
public class TaskRequestConverter {
    public Task createTask(TaskRequest taskRequest){
        if (taskRequest == null){
            return null;
        }

        Task task = new Task();

        task.setTaskName(taskRequest.getTaskName());
        task.setTaskText(taskRequest.getTaskText());
        task.setDeadLine(taskRequest.getDeadLine());

        return task;
    }


    public void updateTask(Task task, TaskRequest taskRequest){
        if (taskRequest.getTaskText() != null){
            task.setTaskText(taskRequest.getTaskText());
        }if (taskRequest.getTaskName() != null){
            task.setTaskName(taskRequest.getTaskName());
        }if (taskRequest.getDeadLine() != null){
            task.setDeadLine(taskRequest.getDeadLine());
        }
    }
}
