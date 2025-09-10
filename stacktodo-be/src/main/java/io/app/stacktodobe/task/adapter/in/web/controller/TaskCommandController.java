package io.app.stacktodobe.task.adapter.in.web.controller;

import io.app.stacktodobe.task.adapter.in.web.dto.CreateTaskDto;
import io.app.stacktodobe.task.application.port.in.CreateTaskUseCase;
import io.app.stacktodobe.task.application.command.TaskCreateCommand;
import io.app.stacktodobe.task.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskCommandController {
    private final CreateTaskUseCase createTaskUsecase;

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskDto request){
        TaskCreateCommand cmd = TaskMapper.toCommand(request);

        createTaskUsecase.create(cmd);

        return ResponseEntity.noContent().build();
    }
}
