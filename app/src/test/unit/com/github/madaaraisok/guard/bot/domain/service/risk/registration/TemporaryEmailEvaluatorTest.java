package com.github.madaaraisok.guard.bot.domain.service.risk.registration;

import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskScore;
import com.github.madaaraisok.guard.bot.domain.model.common.User;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TemporaryEmailEvaluatorTest {

    private final TemporaryEmailEvaluator underTest = new TemporaryEmailEvaluator(Set.of("blacklisted.com"));

    @Test
    void shouldReturnHighRiskScoreWhenDomainIsBlacklisted() {
        // given
        var event = prepareRiskEvent("user@blacklisted.com");

        // when
        var score = underTest.evaluate(event);

        // then
        assertThat(score).isEqualTo(RiskScore.HIGH);
    }

    @Test
    void shouldReturnLowRiskScoreWhenDomainIsNotBlacklisted() {
        // given
        var event = prepareRiskEvent("user@safe.com");

        // when
        var score = underTest.evaluate(event);

        // then
        assertThat(score).isEqualTo(RiskScore.LOW);
    }

    private RiskEvent prepareRiskEvent(String email) {
        return RiskEvent.builder()
                        .user(new User(UUID.randomUUID(), email))
                        .build();
    }

}