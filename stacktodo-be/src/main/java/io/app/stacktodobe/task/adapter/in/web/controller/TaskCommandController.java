package io.app.stacktodobe.task.adapter.in.web.controller;

import io.app.stacktodobe.task.adapter.in.web.dto.CreateTaskDto;
import io.app.stacktodobe.task.application.port.in.CreateTaskUseCase;
import io.app.stacktodobe.task.application.command.CreateTaskCommand;
import io.app.stacktodobe.task.application.port.in.UpdateTaskUseCase;
import io.app.stacktodobe.task.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskCommandController {
    private final CreateTaskUseCase createTaskUsecase;

    private final UpdateTaskUseCase updateTaskUseCase;

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskDto request, Principal principal){
        UUID memberId = UUID.fromString(principal.getName());
        CreateTaskCommand cmd = TaskMapper.toCommand(request);

        createTaskUsecase.create(cmd);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<?> complete(@PathVariable("taskId") UUID taskId, Principal principal){
        UUID memberId = UUID.fromString(principal.getName());

        updateTaskUseCase.complete(TaskMapper.toCompleteCommand(taskId));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taskId}/uncomplete")
    public ResponseEntity<?> unComplete(@PathVariable("taskId") UUID taskId, Principal principal){
        UUID memberId = UUID.fromString(principal.getName());

        updateTaskUseCase.unComplete(TaskMapper.toUnCompleteCommand(taskId));

        return ResponseEntity.ok().build();
    }
}
