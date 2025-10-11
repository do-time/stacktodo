package io.app.stacktodobe.task.adapter.in.web.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateTaskDto(
        // 필수: 제목(255자)
        @jakarta.validation.constraints.NotBlank
        @jakarta.validation.constraints.Size(max = 255)
        String title,

        // 선택: 설명
        String description,

        // 선택: 시작일/시간
        LocalDate startDate,
        LocalTime startTime,

        // 선택: 마감일/시간
        LocalDate dueDate,
        LocalTime dueTime,

        // 선택: 우선순위(1~5). null이면 서비스에서 3으로 디폴트 처리
        @jakarta.validation.constraints.Min(1)
        @jakarta.validation.constraints.Max(5)
        Integer priority
) {
}
