package io.app.stacktodobe.category.application.service;

import io.app.stacktodobe.category.adapter.in.web.dto.CategoryView;
import io.app.stacktodobe.category.application.command.CreateCategoryCommand;
import io.app.stacktodobe.category.application.port.in.CategoryQueryUseCase;
import io.app.stacktodobe.category.application.port.out.CategoryQueryPort;
import io.app.stacktodobe.category.application.port.out.CreateCategoryPort;
import io.app.stacktodobe.category.domain.model.Category;
import io.app.stacktodobe.category.domain.model.CategoryScope;
import io.app.stacktodobe.category.exception.CategoryNotFoundException;
import io.app.stacktodobe.category.mapper.CategoryMapper;
import io.app.stacktodobe.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryQueryService implements CategoryQueryUseCase {
    private final CategoryQueryPort categoryQueryPort;

    @Override
    public CategoryView getPersonalCategory(UUID categoryId, UUID memberId) {
        var category = categoryQueryPort.findByCategoryIdAndMemberId(categoryId, memberId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryId));

        return CategoryMapper.toView(category);
    }

    @Override
    public CategoryView getWorkspaceCategory(UUID categoryId, UUID workspaceId, UUID memberId) {
        // member가 workspace에 속해있는지 판별하는 소스 추가

        var category = categoryQueryPort.findByCategoryIdAndWorkspaceId(categoryId, workspaceId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryId));

        return CategoryMapper.toView(category);
    }

    @Override
    public CategoryView getCommunityCategory(UUID categoryId) {
        var category = categoryQueryPort.findByCategoryIdAndScope(categoryId, CategoryScope.COMMUNITY)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryId));

        return CategoryMapper.toView(category);
    }

    @Override
    public List<CategoryView> getPersonalCategoryList(UUID memberId) {
        return categoryQueryPort.findAllByMemberId(memberId)
                .stream()
                .map(CategoryMapper::toView)
                .toList();
    }

    @Override
    public List<CategoryView> getWorkspaceCategoryList(UUID workspaceId, UUID memberId) {
        return categoryQueryPort.findAllByWorkspaceId(workspaceId)
                .stream()
                .map(CategoryMapper::toView)
                .toList();
    }

    @Override
    public List<CategoryView> getCommunityCategoryList() {
        return categoryQueryPort.findAllByScope(CategoryScope.COMMUNITY)
                .stream()
                .map(CategoryMapper::toView)
                .toList();
    }
}
