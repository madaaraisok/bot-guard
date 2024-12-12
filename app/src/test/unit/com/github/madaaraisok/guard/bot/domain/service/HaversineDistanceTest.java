package com.github.madaaraisok.guard.bot.domain.service;

import org.assertj.core.data.Offset;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class HaversineDistanceTest {

    @ParameterizedTest
    @CsvSource({
        "50.012100, 20.985842, 50.286263, 19.104078, 137.499",
        "53.428543, 14.552812, 50.286263, 19.104078, 468.621",
        "52.406376, 16.925167, 50.286263, 19.104078, 280.109"
    })
    void shouldCalculate(double startLat, double startLon, double endLat, double endLon, double expectedDistance) {
        // given
        var underTest = new HaversineDistance();

        // when
        var result = underTest.calculate(startLat, startLon, endLat, endLon);

        // then
        assertThat(result).isEqualTo(expectedDistance, Offset.offset(0.001));
    }
}