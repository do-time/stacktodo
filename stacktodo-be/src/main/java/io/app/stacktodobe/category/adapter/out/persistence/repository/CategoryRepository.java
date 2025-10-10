package io.app.stacktodobe.category.adapter.out.persistence.repository;

import io.app.stacktodobe.category.adapter.out.persistence.entity.CategoryEntity;
import io.app.stacktodobe.category.domain.model.CategoryScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByNameIgnoreCaseAndMemberId(String name, UUID memberId);

    Optional<CategoryEntity> findByCategoryIdAndMemberId(UUID categoryId, UUID memberId);
    Optional<CategoryEntity> findByCategoryIdAndWorkspaceId(UUID categoryId, UUID workspaceId);
    Optional<CategoryEntity> findByCategoryIdAndScope(UUID categoryId, CategoryScope scope);
    List<CategoryEntity> findAllByMemberId(UUID memberId);
    List<CategoryEntity> findAllByWorkspaceId(UUID workspaceId);
    List<CategoryEntity> findAllByScope(CategoryScope scope);
}
