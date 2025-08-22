package io.app.stacktodobe.task.persistence.repository;

import io.app.stacktodobe.task.persistence.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
