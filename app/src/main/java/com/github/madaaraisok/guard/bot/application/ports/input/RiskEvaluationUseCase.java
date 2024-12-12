package com.github.madaaraisok.guard.bot.application.ports.input;

import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;

public interface RiskEvaluationUseCase {

    RiskEvaluationScore evaluate(RiskEvent riskEvent);

}
