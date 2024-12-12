package com.github.madaaraisok.guard.bot.domain.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DateServiceTest {

    @Test
    void shouldReturnCurrentDate() {
        // Given
        var underTest = new DateService();

        // When
        var result = underTest.currentDate();

        // Then
        assertNotNull(result);
    }

}