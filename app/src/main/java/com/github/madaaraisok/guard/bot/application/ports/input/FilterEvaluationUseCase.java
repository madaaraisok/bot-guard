package com.github.madaaraisok.guard.bot.application.ports.input;

import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvent;

public interface FilterEvaluationUseCase {

    FilterEvaluationScore evaluate(FilterEvent riskEvent);

}
