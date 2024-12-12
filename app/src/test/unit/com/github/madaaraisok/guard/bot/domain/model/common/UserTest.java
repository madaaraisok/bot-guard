package com.github.madaaraisok.guard.bot.domain.model.common;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @ParameterizedTest
    @CsvSource({
        "user@example.com, user@example.com",
        "u.ser@example.com, user@example.com",
        "us.er@example.com, user@example.com",
        "use.r@example.com, user@example.com",
        "u.se.r@example.com, user@example.com",
        "u.s.e.r@example.com, user@example.com"
    })
    void shouldSanitizeEmail(String email, String sanitizedEmail) {
        // when
        var result = new User(UUID.randomUUID(), email).sanitizedEmail();

        // then
        assertThat(result).isEqualTo(sanitizedEmail);
    }

}