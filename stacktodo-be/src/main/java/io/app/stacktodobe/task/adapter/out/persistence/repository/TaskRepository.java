package io.app.stacktodobe.task.adapter.out.persistence.repository;

import io.app.stacktodobe.task.adapter.out.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
