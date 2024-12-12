package com.github.madaaraisok.guard.bot.domain.service.risk.registration;

import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskScore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
class TemporaryEmailEvaluator implements RegistrationRiskEvaluator {

    private final Set<String> blacklistedDomains;

    @Override
    public RiskScore evaluate(RiskEvent event) {
        var domain = getDomain(event.getUser().email());
        return blacklistedDomains.contains(domain) ? RiskScore.HIGH : RiskScore.LOW;
    }

    private String getDomain(String email) {
        return email.substring(email.indexOf("@") + 1);
    }

}