package io.app.stacktodobe.task.application.service;

import io.app.stacktodobe.common.exception.EntityNotFoundException;
import io.app.stacktodobe.task.adapter.in.web.dto.TaskView;
import io.app.stacktodobe.task.application.port.in.TaskQueryUseCase;
import io.app.stacktodobe.task.application.port.out.TaskQueryPort;
import io.app.stacktodobe.task.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskQueryService implements TaskQueryUseCase {
    final private TaskQueryPort taskQueryPort;

    @Override
    public TaskView getTask(UUID taskId, UUID memberId) {
        // 1) member entity get 소스추가
        //var member =
        log.info("Getting task with taskId {}, memberId {}", taskId, memberId);
        var task = taskQueryPort.findByTaskIdAndOwnerId(taskId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + taskId));
        log.info("Task with id {} found", taskId);
        return TaskMapper.toView(task);
    }

    @Override
    public List<TaskView> getListByMember(UUID memberId) {
        return taskQueryPort.findAllByOwnerId(memberId)
                .stream()
                .map(TaskMapper::toView)
                .toList();
    }

    @Override
    public List<TaskView> getListByMember(UUID memberId, LocalDate date) {
        return taskQueryPort.findAllByOwnerIdAndStartDate(memberId, date)
                .stream()
                .map(TaskMapper::toView)
                .toList();
    }

    @Override
    public List<TaskView> getListByMember(UUID memberId, YearMonth ym) {
        return List.of();
    }

    @Override
    public List<TaskView> getListByWorkspace(UUID workspaceId) {
        return taskQueryPort.findAllByWorkspaceId(workspaceId)
                .stream()
                .map(TaskMapper::toView)
                .toList();
    }

    @Override
    public List<TaskView> getListByWorkspace(UUID workspaceId, LocalDate date) {
        return List.of();
    }

    @Override
    public List<TaskView> getListByWorkspace(UUID workspaceId, YearMonth ym) {
        return List.of();
    }
}
