package io.app.stacktodobe.task.application.service;

import io.app.stacktodobe.task.application.command.*;
import io.app.stacktodobe.task.application.port.in.DeleteTaskUseCase;
import io.app.stacktodobe.task.application.port.in.UpdateTaskUseCase;
import io.app.stacktodobe.task.application.port.out.DeleteTaskPort;
import io.app.stacktodobe.task.application.port.out.UpdateTaskPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteTaskService implements DeleteTaskUseCase {
    private final DeleteTaskPort deleteTaskPort;

    @Override
    @Transactional
    public void delete(UUID taskId) {
        var task = deleteTaskPort.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        deleteTaskPort.deleteById(taskId);
    }

    @Override
    public void restore(UUID taskId) {

    }
}
