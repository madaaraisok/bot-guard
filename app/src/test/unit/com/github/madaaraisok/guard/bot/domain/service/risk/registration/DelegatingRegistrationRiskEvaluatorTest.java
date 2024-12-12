package com.github.madaaraisok.guard.bot.domain.service.risk.registration;

import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DelegatingRegistrationRiskEvaluatorTest {

    @Mock
    private RegistrationRiskEvaluator evaluator1;

    @Mock
    private RegistrationRiskEvaluator evaluator2;

    private DelegatingRegistrationRiskEvaluator underTest;

    @BeforeEach
    void setUp() {
        underTest = new DelegatingRegistrationRiskEvaluator(List.of(evaluator1, evaluator2));
    }

    @Test
    void shouldReturnHighestRiskScore() {
        // given
        var event = new RiskEvent();

        given(evaluator1.evaluate(event)).willReturn(new RiskScore(0.5));
        given(evaluator2.evaluate(event)).willReturn(new RiskScore(0.8));

        // when
        var result = underTest.evaluate(event);

        // then
        assertThat(result).isEqualTo(new RiskScore(0.8));
    }

    @Test
    void shouldReturnLowRiskScoreWhenNoEvaluators() {
        // given
        underTest = new DelegatingRegistrationRiskEvaluator(List.of());

        // when
        var result = underTest.evaluate(new RiskEvent());

        // then
        assertThat(result).isEqualTo(RiskScore.LOW);
    }
}