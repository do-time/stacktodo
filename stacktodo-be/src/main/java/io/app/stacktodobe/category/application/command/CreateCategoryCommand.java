package io.app.stacktodobe.category.application.command;

import io.app.stacktodobe.category.domain.model.CategoryScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
public record CreateCategoryCommand(
        @NotBlank
        @Size(max = 100)
        String name,

        String description,

        @NotNull
        CategoryScope scope,

        UUID workspaceId,

        UUID memberId
) {
    public static CreateCategoryCommand of(String name, CategoryScope scope, UUID workspaceId, UUID memberId) {
        return CreateCategoryCommand.builder()
                .name(name)
                .scope(scope)
                .workspaceId(workspaceId)
                .memberId(memberId)
                .build();
    }
}
