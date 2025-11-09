package io.app.stacktodobe.member.domain;

public record Email(String address) {
    private static final String PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public Email {
        // 이메일 검증, 중복확인
//        if (address() == null || !address().matches(PATTERN)) {
//            throw new InvalidCommandException("잘못된 이메일 형식입니다");
//        }
    }
}
