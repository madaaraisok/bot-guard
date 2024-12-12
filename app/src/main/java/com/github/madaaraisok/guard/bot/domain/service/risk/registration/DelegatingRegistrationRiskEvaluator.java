package com.github.madaaraisok.guard.bot.domain.service.risk.registration;

import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskScore;
import com.github.madaaraisok.guard.bot.domain.service.risk.RiskEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DelegatingRegistrationRiskEvaluator implements RiskEvaluator {

    private final List<RegistrationRiskEvaluator> registrationRiskEvaluators;

    @Override
    public RiskScore evaluate(RiskEvent event) {
        return registrationRiskEvaluators.stream()
                                         .map(evaluation -> evaluation.evaluate(event))
                                         .reduce(RiskScore.LOW, RiskScore::max);
    }

    @Override
    public EventType getEventType() {
        return EventType.REGISTRATION;
    }

}
