package io.app.stacktodobe.common.utils;

public class SlugUtils {
    public static String slugify(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Slug source cannot be null");
        }

        // 1. 소문자
        String slug = input.toLowerCase();

        // 2. 한글/영문/숫자 외 제거
        slug = slug.replaceAll("[^a-z0-9가-힣\\s-]", "");

        // 3. 공백을 하이픈으로
        slug = slug.replaceAll("\\s+", "-");

        // 4. 연속된 하이픈 정리
        slug = slug.replaceAll("-{2,}", "-");

        // 5. 앞뒤 하이픈 제거
        slug = slug.replaceAll("^-|-$", "");

        return slug;
    }
}
