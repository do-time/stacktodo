package io.app.stacktodobe.category.adapter.in.web.dto;

import io.app.stacktodobe.category.domain.model.CategoryScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateCategoryDto(
        @NotBlank
        @Size(max = 100)
        String name,

        String description,

        @NotNull
        CategoryScope scope,

        UUID workspaceId,

        UUID memberId
)
{}
