package io.app.stacktodobe.task.application.port.in;

import io.app.stacktodobe.task.adapter.in.web.dto.TaskView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public interface TaskQueryUseCase {
    TaskView getTask(UUID taskId, UUID memberId);
    List<TaskView> getListByMember(UUID memberId);
    List<TaskView> getListByMember(UUID memberId, LocalDate date);
    List<TaskView> getListByMember(UUID memberId, YearMonth month); //YYYY-MM
    List<TaskView> getListByWorkspace(UUID workspaceId);
    List<TaskView> getListByWorkspace(UUID workspaceId, LocalDate date);
    List<TaskView> getListByWorkspace(UUID workspaceId, YearMonth ym);

}
