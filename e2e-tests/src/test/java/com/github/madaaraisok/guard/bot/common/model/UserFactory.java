package com.github.madaaraisok.guard.bot.common.model;

import java.util.Random;

public class UserFactory {

    public static User newUser() {
        return new User(randomEmail());
    }

    public static User newUserWithEmail(String name, String domain) {
        return new User(name + "@" + domain);
    }

    public static User newUserWithEmailDomain(String domain) {
        return new User(randomEmail(domain));
    }

    private static String randomEmail() {
        return randomEmail("gmail.com");
    }

    private static String randomEmail(String domain) {
        return "test." + getString() + "@" + domain;
    }

    public static String randomEmailDomain() {
        return getString() + ".example.com";
    }

    private static String getString() {
        return String.valueOf(new Random().nextInt(9999999 - 1000000) + 1000000);
    }

}
