package io.app.stacktodobe.task.adapter.in.web.controller;

import io.app.stacktodobe.task.adapter.in.web.dto.CreateTaskDto;
import io.app.stacktodobe.task.adapter.in.web.dto.TaskView;
import io.app.stacktodobe.task.application.command.CreateTaskCommand;
import io.app.stacktodobe.task.application.port.in.TaskQueryUseCase;
import io.app.stacktodobe.task.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskQueryController {
    private final TaskQueryUseCase taskQueryUseCase;

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskView> getTask(@PathVariable UUID taskId, Principal principal) {
        UUID memberId = UUID.fromString(principal.getName());
        var view = taskQueryUseCase.getById(memberId, taskId);

        return ResponseEntity.ok(view);
    }

    @GetMapping("/member-day")
    public ResponseEntity<List<TaskView>> getByMemberAndDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Principal principal) {
        UUID memberId = UUID.fromString(principal.getName());
        var view = taskQueryUseCase.listByMemberAndDay(memberId, date);

        return ResponseEntity.ok(view);
    }

    @GetMapping("/member-month")
    public ResponseEntity<List<TaskView>> listByMemberAndMonth(
            @RequestParam String month, // YYYY-MM
            Principal principal) {
        UUID memberId = UUID.fromString(principal.getName());
        YearMonth ym = YearMonth.parse(month);
        return ResponseEntity.ok(taskQueryUseCase.listByMemberAndMonth(memberId, ym));
    }
}
