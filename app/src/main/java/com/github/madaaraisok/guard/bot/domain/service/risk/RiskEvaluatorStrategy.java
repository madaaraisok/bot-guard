package com.github.madaaraisok.guard.bot.domain.service.risk;

import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskScore;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
class RiskEvaluatorStrategy implements RiskEvaluator {

    private final Map<EventType, RiskEvaluator> riskEvaluatorMap = new EnumMap<>(EventType.class);

    RiskEvaluatorStrategy(List<RiskEvaluator> riskEvaluatorList) {
        riskEvaluatorList.forEach(evaluator -> riskEvaluatorMap.put(evaluator.getEventType(), evaluator));
    }

    @Override
    public RiskScore evaluate(RiskEvent event) {
        return riskEvaluatorMap.get(event.getType()).evaluate(event);
    }

    @Override
    public EventType getEventType() {
        return null;
    }

}
