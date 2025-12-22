package io.app.stacktodobe.task.adapter.in.web.controller;

import io.app.stacktodobe.task.adapter.in.web.dto.TaskView;
import io.app.stacktodobe.task.application.port.in.TaskQueryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TaskQueryController {
    private final TaskQueryUseCase taskQueryUseCase;

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskView> getTask(@PathVariable UUID taskId, Principal principal) {
        log.info("Getting task with id {}", taskId);
        //UUID memberId = UUID.fromString(principal.getName());
        UUID memberId = UUID.fromString("d8a95759-91f1-4c67-b2d3-7c7ecbf075ce");
        var view = taskQueryUseCase.getTask(taskId, memberId);

        return ResponseEntity.ok(view);
    }

    @GetMapping("/member-day")
    public ResponseEntity<List<TaskView>> getByMemberAndDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Principal principal) {
        //UUID memberId = UUID.fromString(principal.getName());
        UUID memberId = UUID.fromString("d8a95759-91f1-4c67-b2d3-7c7ecbf075ce");

        var view = taskQueryUseCase.getListByMember(memberId, date);

        return ResponseEntity.ok(view);
    }

    @GetMapping("/member-month")
    public ResponseEntity<List<TaskView>> listByMemberAndMonth(
            @RequestParam String month, // YYYY-MM
            Principal principal) {
        UUID memberId = UUID.fromString(principal.getName());
        YearMonth ym = YearMonth.parse(month);
        return ResponseEntity.ok(taskQueryUseCase.getListByMember(memberId, ym));
    }
}
