package io.app.stacktodobe.notification.domain;

import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.notification.enums.NotificationEntityType;
import io.app.stacktodobe.notification.enums.NotificationStatus;
import lombok.Data;

@Data
public class NotificationMessage {

    private Long id;
    private NotificationEntityType entityType;
    private MemberEntity receiver;
    private String payload;
    private NotificationStatus status = NotificationStatus.PENDING;

}
