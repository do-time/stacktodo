package io.app.stacktodobe.task.adapter.in.web.dto;

import io.app.stacktodobe.task.domain.model.TaskStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Builder
public record TaskView(
        UUID taskId,                 // 외부 공개 ID
        UUID workspaceId,            // 소속 워크스페이스
        UUID categoryId,             // 카테고리 (선택)
        UUID ownerId,                // 담당자 (필수)
        UUID templateCategoryId,     // 템플릿에서 복사된 경우

        String title,                // 필수, 255자 제한
        String description,          // 선택
        LocalDate startDate,
        LocalTime startTime,
        LocalDate dueDate,
        LocalTime dueTime,
        int priority,                // 1~5, 기본값 3
        int percentComplete,         // 0~100, 기본값 0
        boolean routine,             // 루틴 여부
        String recurrenceRule,       // RRULE or JSON
        TaskStatus status           // PENDING, IN_PROGRESS, COMPLETED
) {
}
