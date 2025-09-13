package io.app.stacktodobe.category.adapter.out;

import io.app.stacktodobe.category.adapter.out.persistence.repository.CategoryRepository;
import io.app.stacktodobe.category.application.port.out.CreateCategoryPort;
import io.app.stacktodobe.category.domain.model.Category;
import io.app.stacktodobe.category.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CategoryRepositoryAdapter implements CreateCategoryPort {
    private final CategoryRepository categoryRepository;

    @Override
    public Category createPersonal(Category category) {
        if(categoryRepository.existsByNameIgnoreCaseAndMemberId(category.getName(), category.getMemberId())) {
            throw new DataIntegrityViolationException(
                    String.format("Category already exists: name='%s', memberId='%s'", category.getName(), category.getMemberId())
            );
        }

        var entity = CategoryMapper.toEntity(category);
        var created = categoryRepository.save(entity);

        return CategoryMapper.toDomain(created);
    }
}
