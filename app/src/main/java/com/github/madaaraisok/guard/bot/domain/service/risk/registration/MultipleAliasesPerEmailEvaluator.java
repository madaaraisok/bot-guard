package com.github.madaaraisok.guard.bot.domain.service.risk.registration;

import com.github.madaaraisok.guard.bot.application.ports.output.UserOutputPort;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskScore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class MultipleAliasesPerEmailEvaluator implements RegistrationRiskEvaluator {

    private final UserOutputPort userOutputPort;

    @Override
    public RiskScore evaluate(RiskEvent event) {
        var aliasCount = userOutputPort.countBySanitizedEmail(event.getUser().sanitizedEmail());
        log.info("Found {} users with the same sanitized email {}", aliasCount, event.getUser().email());

        if (isLowRisk(aliasCount)) {
            return RiskScore.LOW;
        } else if (isMediumRisk(aliasCount)) {
            return new RiskScore(0.5);
        } else {
            return RiskScore.HIGH;
        }
    }

    private boolean isMediumRisk(long aliasCount) {
        return aliasCount > 1 && aliasCount < 5;
    }

    private boolean isLowRisk(long aliasCount) {
        return aliasCount < 2;
    }

}
