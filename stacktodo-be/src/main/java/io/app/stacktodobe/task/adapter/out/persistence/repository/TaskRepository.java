package io.app.stacktodobe.task.adapter.out.persistence.repository;

import io.app.stacktodobe.task.adapter.out.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Optional<TaskEntity> findByTaskId(UUID taskId);
    Optional<TaskEntity> findByTaskIdAndOwnerId(UUID taskId, UUID ownerId);

    List<TaskEntity> findAllByOwnerId(UUID ownerId);
    List<TaskEntity> findAllByOwnerIdAndStartDate(UUID ownerId, LocalDate date);
}
