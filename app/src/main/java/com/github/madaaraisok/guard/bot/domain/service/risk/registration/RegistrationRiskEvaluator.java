package com.github.madaaraisok.guard.bot.domain.service.risk.registration;

import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskScore;

interface RegistrationRiskEvaluator {

    RiskScore evaluate(RiskEvent event);

}
