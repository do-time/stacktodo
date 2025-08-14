package io.app.stacktodobe.notification.enums;

/**
 * 알림 전송 상태
 */
public enum NotificationStatus {
    PENDING,    // 전송 대기
    SENT,       // 전송 완료
    FAILED,     // 전송 실패
    CANCELED    // 취소됨
}