package io.app.stacktodobe.category.adapter.in.web.dto;

import io.app.stacktodobe.category.domain.model.CategoryScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CategoryView(
        @NotBlank
        @Size(max = 100)
        String name,

        String description,

        @NotNull
        CategoryScope scope,

        UUID workspaceId,

        @NotNull
        UUID memberId,

        @NotNull
        UUID categoryId
)
{}
