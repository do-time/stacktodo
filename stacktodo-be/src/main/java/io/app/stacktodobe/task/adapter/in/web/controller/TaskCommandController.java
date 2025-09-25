package io.app.stacktodobe.task.adapter.in.web.controller;

import io.app.stacktodobe.task.adapter.in.web.dto.CreateTaskDto;
import io.app.stacktodobe.task.adapter.in.web.dto.UpdateTaskDto;
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
    public ResponseEntity<Void> createTask(@RequestBody CreateTaskDto request, Principal principal) {
        UUID memberId = UUID.fromString(principal.getName());
        CreateTaskCommand cmd = TaskMapper.toCommand(request);

        createTaskUsecase.create(cmd);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<Void> complete(@PathVariable("taskId") UUID taskId, Principal principal) {
        UUID memberId = UUID.fromString(principal.getName());

        updateTaskUseCase.complete(TaskMapper.toCompleteCommand(taskId));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId}/uncomplete")
    public ResponseEntity<Void> unComplete(@PathVariable("taskId") UUID taskId, Principal principal) {
        UUID memberId = UUID.fromString(principal.getName());

        updateTaskUseCase.unComplete(TaskMapper.toUnCompleteCommand(taskId));

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{taskId}/title")
    public ResponseEntity<Void> changeTitle(@PathVariable("taskId") UUID taskId, @RequestBody UpdateTaskDto request, Principal principal) {
        UUID memberId = UUID.fromString(principal.getName());

        updateTaskUseCase.updateTask(TaskMapper.toUpdateCommand(taskId, request));

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(@PathVariable("taskId") UUID taskId, @RequestBody UpdateTaskDto request, Principal principal) {
        UUID memberId = UUID.fromString(principal.getName());

        updateTaskUseCase.updateTask(TaskMapper.toUpdateCommand(taskId, request));

        return ResponseEntity.noContent().build();
    }
}
