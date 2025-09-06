package io.app.stacktodobe.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TestSourceGenerator {

    public static String generateEmail() {
        return "email" + System.currentTimeMillis() + "@example.com";
    }

    public static String generatePassword() {
        return generateComplexPassword(12);

    }

    private static String generateComplexPassword(int length) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^*?";

        String all = upper + lower + digits + special;
        StringBuilder password = new StringBuilder();

        password.append(upper.charAt((int) (Math.random() * upper.length())));
        password.append(lower.charAt((int) (Math.random() * lower.length())));
        password.append(digits.charAt((int) (Math.random() * digits.length())));
        password.append(special.charAt((int) (Math.random() * special.length())));

        for (int i = 4; i < length; i++) {
            password.append(all.charAt((int) (Math.random() * all.length())));
        }

        return password.toString();
    }

    public static String generateUsername() {
        return UUID.randomUUID().toString();
    }

    public static String generateProfileImage() {
        return "https://example.com/profile/" + UUID.randomUUID().toString();
    }
    public static String generatePhoneNumber() {
        return "+82-" + (100000000 + (int) (Math.random() * 900000000));
    }

    public static String generateWorkspaceName() {
        String random = Long.toString(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE), 16); // 0-9a-z
        String name = "ws-" + random;
        return name.length() <= 100 ? name : name.substring(0, 100);
    }
}
