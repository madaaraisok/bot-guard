package com.github.madaaraisok.guard.bot.domain.service.filter;

import com.github.madaaraisok.guard.bot.application.ports.input.FilterEvaluationUseCase;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvent;
import com.github.madaaraisok.guard.bot.domain.service.DateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FilterEvaluationService implements FilterEvaluationUseCase {

    private final FilterEvaluator filterEvaluator;
    private final DateService dateService;

    @Override
    public FilterEvaluationScore evaluate(FilterEvent filterEvent) {

        var createdAt = dateService.currentDate();
        var filterScore = filterEvaluator.evaluate(filterEvent);

        return FilterEvaluationScore.builder()
                                    .id(filterEvent.getId())
                                    .type(filterEvent.getType())
                                    .risk(filterScore.risk())
                                    .createdAt(createdAt)
                                    .build();
    }

}
