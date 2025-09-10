package io.app.stacktodobe.category.application.service;

import io.app.stacktodobe.category.application.command.CreateCategoryCommand;
import io.app.stacktodobe.category.application.port.in.CreateCategoryUseCase;
import io.app.stacktodobe.category.application.port.out.CreateCategoryPort;
import io.app.stacktodobe.category.domain.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateCategoryService implements CreateCategoryUseCase {
    private final CreateCategoryPort createCategoryPort;

    @Override
    public void createPersonal(CreateCategoryCommand command) {
        var category = Category.ofPersonal(command.memberId(), command.name(), command.description());

        createCategoryPort.createPersonal(category);
    }
}
