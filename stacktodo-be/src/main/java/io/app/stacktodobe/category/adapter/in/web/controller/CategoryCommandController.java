package io.app.stacktodobe.category.adapter.in.web.controller;

import io.app.stacktodobe.category.adapter.in.web.dto.CreateCategoryDto;
import io.app.stacktodobe.category.application.command.CreateCategoryCommand;
import io.app.stacktodobe.category.application.port.in.CreateCategoryUseCase;
import io.app.stacktodobe.category.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryCommandController {
    private final CreateCategoryUseCase createCategoryUseCase;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody CreateCategoryDto request){
        var command = CategoryMapper.toCommand(request);

        createCategoryUseCase.createPersonal(command);

        return ResponseEntity.noContent().build();
    }
}
