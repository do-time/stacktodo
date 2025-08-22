package io.app.stacktodobe.category.persistence.repository;

import io.app.stacktodobe.category.persistence.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
