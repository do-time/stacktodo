package io.app.stacktodobe.task.application.port.in;

import io.app.stacktodobe.task.adapter.in.web.dto.TaskView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public interface TaskQueryUseCase {
    TaskView getById(UUID taskId, UUID memberId);
    List<TaskView> listByOwner(UUID memberId);
    List<TaskView> listByWorkspace(UUID workspaceId);
    List<TaskView> listByWorkspaceAndDay(UUID workspaceId, LocalDate date);
    List<TaskView> listByWorkspaceAndMonth(UUID workspaceId, YearMonth ym);
    List<TaskView> listByMemberAndDay(UUID memberId, LocalDate date);
    List<TaskView> listByMemberAndMonth(UUID memberId, YearMonth month); //YYYY-MM
}
