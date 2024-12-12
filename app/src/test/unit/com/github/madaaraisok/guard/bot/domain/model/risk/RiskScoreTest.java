package com.github.madaaraisok.guard.bot.domain.model.risk;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class RiskScoreTest {

    @ParameterizedTest
    @CsvSource({
        "1.0, 2.0, 2.0",
        "2.0, 1.0, 2.0",
        "3.0, 3.0, 3.0"
    })
    void shouldReturnMaxRiskScore(double a, double b, double c) {
        // given
        RiskScore riskScoreA = new RiskScore(a);
        RiskScore riskScoreB = new RiskScore(b);

        // when
        var result = RiskScore.max(riskScoreA, riskScoreB);

        // then
        assertThat(result).isEqualTo(new RiskScore(c));
    }

}