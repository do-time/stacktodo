package io.app.stacktodobe.utils;

import java.util.UUID;

public class TestSourceGenerator {

    public static String generateEmail() {
        return "email" + System.currentTimeMillis() + "@example.com";
    }

    public static String generatePassword() {
        return generateComplexPassword(8);

    }

    private static String generateComplexPassword(int length) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*()-_=+[]{}|;:,.<>?";

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

    public static String generateName() {
        return "name " + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generateProfileImage() {
        return "https://example.com/profile/" + UUID.randomUUID().toString();
    }
    public static String generatePhoneNumber() {
        return "+82-" + (100000000 + (int) (Math.random() * 900000000));
    }
}
