package io.app.stacktodobe.category.mapper;

import io.app.stacktodobe.category.adapter.in.web.dto.CreateCategoryDto;
import io.app.stacktodobe.category.adapter.out.persistence.entity.CategoryEntity;
import io.app.stacktodobe.category.application.command.CreateCategoryCommand;
import io.app.stacktodobe.category.domain.model.Category;

public final class CategoryMapper {
    public static CategoryEntity toEntity(Category category) {
        return CategoryEntity.builder()
                .name(category.getName())
                .description(category.getDescription())
                .slug(category.getSlug())
                .scope(category.getScope())
                .workspaceId(category.getWorkspaceId())
                .memberId(category.getMemberId())
                .build();
    }

    public static Category toDomain(CategoryEntity entity) {
        return Category.reconstruct(entity.getCategoryId(), entity.getName(), entity.getDescription(), entity.getSlug(), entity.getScope(), entity.getWorkspaceId(), entity.getMemberId());
    }

    public static CreateCategoryCommand toCommand(CreateCategoryDto dto){
        return CreateCategoryCommand.builder()
                .name(dto.name())
                .description(dto.description())
                .scope(dto.scope())
                .workspaceId(dto.workspaceId())
                .memberId(dto.memberId())
                .build();
    }
}
