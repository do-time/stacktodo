package io.app.stacktodobe.category.application.port.out;

import io.app.stacktodobe.category.domain.model.Category;
import io.app.stacktodobe.category.domain.model.CategoryScope;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryQueryPort {
    Optional<Category> findByCategoryIdAndMemberId(UUID categoryId, UUID memberId);
    Optional<Category> findByCategoryIdAndWorkspaceId(UUID categoryId, UUID workspaceId);
    Optional<Category> findByCategoryIdAndScope(UUID categoryId, CategoryScope scope);
    List<Category> findAllByMemberId(UUID memberId);
    List<Category> findAllByWorkspaceId(UUID workspaceId);
    List<Category> findAllByScope(CategoryScope scope);
}
