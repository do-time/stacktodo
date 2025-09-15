package io.app.stacktodobe.category.application.port.out;

import io.app.stacktodobe.category.domain.model.Category;

public interface CreateCategoryPort {
    Category createPersonal(Category category);
}
