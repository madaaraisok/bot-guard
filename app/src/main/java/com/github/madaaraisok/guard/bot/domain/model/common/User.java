package com.github.madaaraisok.guard.bot.domain.model.common;

import java.util.UUID;

public record User(UUID id, String email) {

    public String sanitizedEmail() {
        return sanitizeEmail(email);
    }

    private static String sanitizeEmail(String email) {
        var splitEmail = email.split("@");
        var name = splitEmail[0].replace(".", "");
        return name + "@" + splitEmail[1];
    }

}
