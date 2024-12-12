package com.github.madaaraisok.guard.bot.infrastructure.adapters.input.rest.filter;

import com.github.madaaraisok.guard.bot.application.dto.filter.FilterEvaluationRequestDto;
import com.github.madaaraisok.guard.bot.application.dto.filter.FilterEvaluationResponseDto;
import com.github.madaaraisok.guard.bot.application.ports.input.FilterEvaluationUseCase;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Filters")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
class FilterEvaluationRestAdapter {

    private final FilterEvaluationUseCase filterEvaluationUseCase;
    private final FilterEvaluationMapper filterEvaluationMapper;

    @Operation(summary = "Used to block bots in the request chain.")
    @PostMapping("/filters")
    FilterEvaluationResponseDto filterEvaluation(@Valid @RequestBody FilterEvaluationRequestDto riskEvaluationRequest) {
        FilterEvent filterEvent = filterEvaluationMapper.toFilterEvent(riskEvaluationRequest);
        FilterEvaluationScore score = filterEvaluationUseCase.evaluate(filterEvent);
        return filterEvaluationMapper.toFilterEvaluationResponse(score);
    }

}
