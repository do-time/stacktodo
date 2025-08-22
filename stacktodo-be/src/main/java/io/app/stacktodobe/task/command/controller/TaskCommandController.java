package io.app.stacktodobe.task.command.controller;

import io.app.stacktodobe.task.domain.usecase.TaskCommandUsecase;
import io.app.stacktodobe.task.presentation.command.TaskCreateCommand;
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
    private final TaskCommandUsecase taskCommandUsecase;

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskCreateCommand command){
        taskCommandUsecase.createTask(command);
        return ResponseEntity.noContent().build();
    }
}
