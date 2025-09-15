package io.app.stacktodobe.category.application.port.in;

import io.app.stacktodobe.category.application.command.CreateCategoryCommand;

public interface CreateCategoryUseCase {
    void createPersonal( CreateCategoryCommand command);
}
