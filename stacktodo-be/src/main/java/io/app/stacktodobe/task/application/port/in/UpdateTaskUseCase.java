package io.app.stacktodobe.task.application.port.in;

import io.app.stacktodobe.task.application.command.*;

import java.util.UUID;

public interface UpdateTaskUseCase {
    void complete(CompleteTaskCommand cmd);
    void unComplete(UnCompleteTaskCommand cmd);
    void changeTitle(ChangeTitleCommand cmd);
    void changeDescription(ChangeDescriptionCommand cmd);
    void updateTask(UpdateTaskCommand command);
}
