package io.app.stacktodobe.task.application.command;

import io.app.stacktodobe.task.adapter.in.web.dto.CreateTaskDto;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Builder
public record TaskCreateCommand(

        // 필수: 제목(255자)
        @jakarta.validation.constraints.NotBlank
        @jakarta.validation.constraints.Size(max = 255)
        String title,

        // 선택: 설명
        String description,

        // 필수: 소속 워크스페이스
        @jakarta.validation.constraints.NotNull
        UUID workspaceId,

        // 선택: 카테고리
        UUID categoryId,

        // 필수: 담당자(소유자)
        UUID ownerId,

        // 선택: 템플릿에서 복사한 경우의 템플릿(Category) ID
        UUID templateId,

        // 선택: 마감일/시간
        LocalDate dueDate,
        LocalTime dueTime,

        // 선택: 우선순위(1~5). null이면 서비스에서 3으로 디폴트 처리
        @jakarta.validation.constraints.Min(1)
        @jakarta.validation.constraints.Max(5)
        Integer priority,

        // 선택: 완료 여부. null이면 서비스에서 false 처리
        Boolean isComplete,

        // 선택: 루틴 여부. null이면 서비스에서 false 처리
        Boolean isRoutine,

        // 선택: 반복 규칙(RRULE 등)
        @jakarta.validation.constraints.Size(max = 255)
        String recurrenceRule
) {}