package io.app.stacktodobe.task.application.service;

import io.app.stacktodobe.common.exception.EntityNotFoundException;
import io.app.stacktodobe.task.adapter.in.web.dto.TaskView;
import io.app.stacktodobe.task.application.port.in.TaskQueryUseCase;
import io.app.stacktodobe.task.application.port.out.TaskQueryPort;
import io.app.stacktodobe.task.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskQueryService implements TaskQueryUseCase {
    private TaskQueryPort taskQueryPort;

    @Override
    public TaskView getById(UUID taskId, UUID memberId) {
        // 1) member entity get 소스추가
        //var member =

        var task = taskQueryPort.findByTaskIdAndOwnerId(taskId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + taskId));

        return TaskMapper.toView(task);
    }

    @Override
    public List<TaskView> listByOwner(UUID memberId) {
        return taskQueryPort.findAllByOwnerId(memberId)
                .stream()
                .map(TaskMapper::toView)
                .toList();
    }

    @Override
    public List<TaskView> listByWorkspace(UUID workspaceId) {
        return taskQueryPort.findAllByWorkspaceId(workspaceId)
                .stream()
                .map(TaskMapper::toView)
                .toList();
    }

    @Override
    public List<TaskView> listByWorkspaceAndDay(UUID workspaceId, LocalDate date) {
        return List.of();
    }

    @Override
    public List<TaskView> listByWorkspaceAndMonth(UUID workspaceId, YearMonth ym) {
        return List.of();
    }

    @Override
    public List<TaskView> listByMemberAndDay(UUID memberId, LocalDate date) {
        return taskQueryPort.findAllByOwnerIdAndStartDate(memberId, date)
                .stream()
                .map(TaskMapper::toView)
                .toList();
    }

    @Override
    public List<TaskView> listByMemberAndMonth(UUID memberId, YearMonth ym) {
        return List.of();
    }


}
