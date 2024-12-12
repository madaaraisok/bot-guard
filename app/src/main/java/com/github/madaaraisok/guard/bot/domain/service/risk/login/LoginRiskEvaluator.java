package com.github.madaaraisok.guard.bot.domain.service.risk.login;

import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskScore;

interface LoginRiskEvaluator {

    RiskScore evaluate(RiskEvent event);

}
