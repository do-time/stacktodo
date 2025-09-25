package io.app.stacktodobe.task.application.port.out;

import io.app.stacktodobe.task.domain.model.Task;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskQueryPort {
    Optional<Task> findByTaskIdAndOwnerId(UUID taskId, UUID ownerId);
    List<Task> findAllByOwnerId(UUID ownerId);
    List<Task> findAllByWorkspaceId(UUID workspaceId);
    List<Task> findAllByOwnerIdAndStartDate(UUID ownerId, LocalDate date);
    List<Task> findAllByOwnerIdAndMonth(UUID ownerId, YearMonth month);
}
