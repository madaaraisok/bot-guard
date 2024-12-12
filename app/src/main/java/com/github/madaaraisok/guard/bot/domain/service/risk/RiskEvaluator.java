package com.github.madaaraisok.guard.bot.domain.service.risk;

import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskScore;

public interface RiskEvaluator {

    RiskScore evaluate(RiskEvent event);

    EventType getEventType();

}
