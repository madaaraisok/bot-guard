package com.github.madaaraisok.guard.bot.domain.service.risk;

import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskScore;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class RiskEvaluatorStrategyTest {

    @Test
    void shouldChooseCorrectRiskEvaluator() {
        // given
        var riskEvent = mock(RiskEvent.class);
        var riskScore = mock(RiskScore.class);
        var riskEvaluator1 = mock(RiskEvaluator.class);
        var riskEvaluator2 = mock(RiskEvaluator.class);

        given(riskEvaluator1.getEventType()).willReturn(EventType.LOGIN);
        given(riskEvaluator1.evaluate(riskEvent)).willReturn(riskScore);
        given(riskEvaluator2.getEventType()).willReturn(EventType.REGISTRATION);
        given(riskEvent.getType()).willReturn(EventType.LOGIN);

        var riskEvaluatorStrategy = new RiskEvaluatorStrategy(List.of(
            riskEvaluator1, riskEvaluator2
        ));

        // when
        var result = riskEvaluatorStrategy.evaluate(riskEvent);

        // then
        assertThat(result).isEqualTo(riskScore);

        verify(riskEvaluator1).evaluate(riskEvent);
        verify(riskEvaluator2, never()).evaluate(riskEvent);
    }

}