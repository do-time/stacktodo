package io.app.stacktodobe.task.domain.model;

import io.app.stacktodobe.task.application.command.TaskCreateCommand;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

public class Task {
    private UUID taskId;                 // 외부 공개 ID
    private UUID workspaceId;            // 소속 워크스페이스
    private UUID categoryId;             // 카테고리 (선택)
    private UUID ownerId;                // 담당자 (필수)
    private UUID templateCategoryId;     // 템플릿에서 복사된 경우

    private String title;                // 필수, 255자 제한
    private String description;          // 선택
    private LocalDate dueDate;
    private LocalTime dueTime;
    private int priority;                // 1~5, 기본값 3
    private int percentComplete;         // 0~100, 기본값 0
    private boolean routine;             // 루틴 여부
    private String recurrenceRule;       // RRULE or JSON
    private TaskStatus status;           // PENDING, IN_PROGRESS, COMPLETED

    private Task(UUID taskId,
                 UUID workspaceId,
                 UUID categoryId,
                 UUID ownerId,
                 UUID templateCategoryId,
                 String title,
                 String description,
                 LocalDate dueDate,
                 LocalTime dueTime,
                 int priority,
                 int percentComplete,
                 boolean routine,
                 String recurrenceRule,
                 TaskStatus status) {

        this.taskId = taskId;
        this.workspaceId = Objects.requireNonNull(workspaceId, "workspaceId");
        this.categoryId = categoryId;
        this.ownerId = Objects.requireNonNull(ownerId, "ownerId");
        this.templateCategoryId = templateCategoryId;
        this.title = validateTitle(title);
        this.description = description;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.priority = normalizePriority(priority);
        this.percentComplete = clampPercent(percentComplete);
        this.routine = routine;
        this.recurrenceRule = recurrenceRule;
        this.status = Objects.requireNonNull(status, "status");

        enforceStatusPercentInvariant();
    }

    public static Task create(UUID workspaceId,
                              UUID ownerId,
                              UUID categoryId,
                              UUID templateCategoryId,
                              TaskCreateCommand cmd) {
        return new Task(
                UUID.randomUUID(),
                workspaceId,
                categoryId,
                ownerId,
                templateCategoryId,
                cmd.title(),
                cmd.description(),
                cmd.dueDate(),
                cmd.dueTime(),
                cmd.priority() != null ? cmd.priority() : 3,
                0,
                cmd.isRoutine() != null && cmd.isRoutine(),
                cmd.recurrenceRule(),
                TaskStatus.PENDING
        );
    }

    public static Task reconstruct(
            UUID taskId,
            UUID workspaceId,
            UUID categoryId,
            UUID ownerId,
            UUID templateCategoryId,
            String title,
            String description,
            LocalDate dueDate,
            LocalTime dueTime,
            int priority,
            int percentComplete,
            boolean routine,
            String recurrenceRule,
            TaskStatus status
    ) {
        return new Task(
                taskId,
                workspaceId,
                categoryId,
                ownerId,
                templateCategoryId,
                title,
                description,
                dueDate,
                dueTime,
                priority,
                percentComplete,
                routine,
                recurrenceRule,
                status
        );
    }

    // ====== 도메인 로직 ======

    /** 완료 처리: 상태 COMPLETED, percent=100 */
    public void complete() {
        this.status = TaskStatus.COMPLETED;
        this.percentComplete = 100;
    }

    /** 완료 취소: 진행 중으로 되돌림 */
    public void unComplete() {
        if(this.status == TaskStatus.COMPLETED) {
            return;
        }
        this.status = TaskStatus.PENDING;
    }

    /** 진행률 업데이트: 0→PENDING, 1~99→IN_PROGRESS, 100→COMPLETED */
    public void updateProgress(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("percentComplete는 0~100 사이여야 합니다.");
        }
        this.percentComplete = percent;
        if (percent == 100) {
            this.status = TaskStatus.COMPLETED;
        } else if (percent == 0) {
            this.status = TaskStatus.PENDING;
        } else {
            this.status = TaskStatus.IN_PROGRESS;
        }
    }

    public void changeTitle(String newTitle) {
        this.title = validateTitle(newTitle);
    }

    public void changeDescription(String newDescription) {
        this.description = newDescription;
    }

    public void reschedule(LocalDate newDueDate, LocalTime newDueTime) {
        this.dueDate = newDueDate;
        this.dueTime = newDueTime;
    }

    public void changePriority(int newPriority) {
        this.priority = normalizePriority(newPriority);
    }

    // ====== 불변식/검증 ======
    private void enforceStatusPercentInvariant() {
        // status와 percent의 일관성 강제
        switch (this.status) {
            case COMPLETED -> this.percentComplete = 100;
            case PENDING -> {
                if (this.percentComplete > 0) {
                    this.status = TaskStatus.IN_PROGRESS;
                }
            }
            case IN_PROGRESS -> {
                if (this.percentComplete == 0) {
                    this.status = TaskStatus.PENDING;
                } else if (this.percentComplete == 100) {
                    this.status = TaskStatus.COMPLETED;
                }
            }
        }
    }

    private static String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title은 비워둘 수 없습니다.");
        }
        if (title.length() > 255) {
            throw new IllegalArgumentException("title은 255자를 넘을 수 없습니다.");
        }
        return title;
    }

    private static int normalizePriority(int p) {
        return (p >= 1 && p <= 5) ? p : 3;
    }

    private static int clampPercent(int v) {
        return Math.max(0, Math.min(100, v));
    }

    // ====== Getter ======
    public UUID getTaskId() { return taskId; }
    public UUID getWorkspaceId() { return workspaceId; }
    public UUID getCategoryId() { return categoryId; }
    public UUID getOwnerId() { return ownerId; }
    public UUID getTemplateCategoryId() { return templateCategoryId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalTime getDueTime() { return dueTime; }
    public int getPriority() { return priority; }
    public int getPercentComplete() { return percentComplete; }
    public boolean isRoutine() { return routine; }
    public String getRecurrenceRule() { return recurrenceRule; }
    public TaskStatus getStatus() { return status; }
}