package io.app.stacktodobe.category.adapter.out.persistence.repository;

import io.app.stacktodobe.category.adapter.out.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByNameIgnoreCaseAndMemberId(String name, UUID memberId);
}
