package io.app.stacktodobe.task.application.port.in;

import io.app.stacktodobe.task.application.command.ChangeDescriptionCommand;
import io.app.stacktodobe.task.application.command.ChangeTitleCommand;
import io.app.stacktodobe.task.application.command.CompleteTaskCommand;
import io.app.stacktodobe.task.application.command.UnCompleteTaskCommand;

public interface UpdateTaskUseCase {
    void complete(CompleteTaskCommand cmd);
    void unComplete(UnCompleteTaskCommand cmd);
    void changeTitle(ChangeTitleCommand cmd);
    void changeDescription(ChangeDescriptionCommand cmd);
}
