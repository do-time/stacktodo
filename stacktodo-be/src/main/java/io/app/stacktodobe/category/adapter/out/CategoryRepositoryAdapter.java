package io.app.stacktodobe.category.adapter.out;

import io.app.stacktodobe.category.adapter.out.persistence.repository.CategoryRepository;
import io.app.stacktodobe.category.application.port.out.CategoryQueryPort;
import io.app.stacktodobe.category.application.port.out.CreateCategoryPort;
import io.app.stacktodobe.category.domain.model.Category;
import io.app.stacktodobe.category.domain.model.CategoryScope;
import io.app.stacktodobe.category.mapper.CategoryMapper;
import io.app.stacktodobe.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CategoryRepositoryAdapter implements CreateCategoryPort, CategoryQueryPort {
    private final CategoryRepository categoryRepository;

    @Override
    public Category createPersonal(Category category) {
        // service에서 모두 체크하도록 하자
        if(categoryRepository.existsByNameIgnoreCaseAndMemberId(category.getName(), category.getMemberId())) {
            throw new DataIntegrityViolationException(
                    String.format("Category already exists: name='%s', memberId='%s'", category.getName(), category.getMemberId())
            );
        }

        var entity = CategoryMapper.toEntity(category);
        var created = categoryRepository.save(entity);

        return CategoryMapper.toDomain(created);
    }

    @Override
    public Optional<Category> findByCategoryIdAndMemberId(UUID categoryId, UUID memberId) {
        return categoryRepository.findByCategoryIdAndMemberId(categoryId, memberId)
                .map(CategoryMapper::toDomain);
    }

    @Override
    public Optional<Category> findByCategoryIdAndWorkspaceId(UUID categoryId, UUID workspaceId) {
        return categoryRepository.findByCategoryIdAndWorkspaceId(categoryId, workspaceId)
                .map(CategoryMapper::toDomain);
    }

    @Override
    public Optional<Category> findByCategoryIdAndScope(UUID categoryId, CategoryScope scope) {
        return categoryRepository.findByCategoryIdAndScope(categoryId, scope)
                .map(CategoryMapper::toDomain);
    }

    @Override
    public List<Category> findAllByMemberId(UUID memberId) {
        return categoryRepository.findAllByMemberId(memberId)
                .stream()
                .map(CategoryMapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findAllByWorkspaceId(UUID workspaceId) {
        return categoryRepository.findAllByWorkspaceId(workspaceId)
                .stream()
                .map(CategoryMapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findAllByScope(CategoryScope scope) {
        return categoryRepository.findAllByScope(scope)
                .stream()
                .map(CategoryMapper::toDomain)
                .toList();
    }
}
