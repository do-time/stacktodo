package io.app.stacktodobe.task.domain.model;

import io.app.stacktodobe.task.adapter.out.persistence.entity.TaskStatus;
import io.app.stacktodobe.task.application.command.TaskCreateCommand;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Builder
public record Task(
        Long id,                   // DB PK (infra가 채움)
        UUID publicId,             // 외부 공개 ID
        UUID workspaceId,    // 소속 워크스페이스
        UUID categoryId,     // 카테고리 (선택)
        UUID ownerId,        // 담당자 (필수)
        UUID templateCategoryId, // 템플릿에서 복사된 경우

        String title,              // 필수, 255자 제한
        String description,        // 선택
        LocalDate dueDate,
        LocalTime dueTime,
        int priority,              // 1~5, 기본값 3
        int percentComplete,       // 0~100, 기본값 0
        boolean routine,           // 루틴 여부
        String recurrenceRule,     // RRULE or JSON
        TaskStatus status          // PENDING, IN_PROGRESS, COMPLETED, CANCELLED
) {
    public static Task of(UUID publicId,
                          UUID workspaceId,
                          UUID ownerId,
                          UUID categoryId,
                          UUID templateCategoryId,
                          TaskCreateCommand cmd) {
        return Task.builder()
                .id(null) // 새 생성이므로 DB PK는 null
                .publicId(publicId)
                .workspaceId(workspaceId)
                .ownerId(ownerId)
                .categoryId(categoryId)
                .templateCategoryId(templateCategoryId)
                .title(cmd.title())
                .description(cmd.description())
                .dueDate(cmd.dueDate())
                .dueTime(cmd.dueTime())
                .priority(cmd.priority() != null ? cmd.priority() : 3)
                .percentComplete(0) // 새 생성 기본값
                .routine(cmd.isRoutine() != null && cmd.isRoutine())
                .recurrenceRule(cmd.recurrenceRule())
                .status(TaskStatus.PENDING)
                .build();
    }}
