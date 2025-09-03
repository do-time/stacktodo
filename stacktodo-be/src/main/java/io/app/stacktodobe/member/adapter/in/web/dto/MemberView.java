package io.app.stacktodobe.member.adapter.in.web.dto;

import java.util.UUID;

public record MemberView(
        Long id,
        UUID memberId,
        String email,
        String name,
        String profileImageUrl
) {
}
