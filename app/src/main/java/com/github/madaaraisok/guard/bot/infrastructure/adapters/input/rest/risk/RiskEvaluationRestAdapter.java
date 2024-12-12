package com.github.madaaraisok.guard.bot.infrastructure.adapters.input.rest.risk;

import com.github.madaaraisok.guard.bot.application.dto.risk.RiskEvaluationRequestDto;
import com.github.madaaraisok.guard.bot.application.dto.risk.RiskEvaluationResponseDto;
import com.github.madaaraisok.guard.bot.application.ports.input.RiskEvaluationUseCase;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Risks")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
class RiskEvaluationRestAdapter {

    private final RiskEvaluationUseCase riskEvaluationUseCase;
    private final RiskEvaluationMapper riskEvaluationMapper;

    @Operation(summary = "Used to request a risk evaluation.")
    @PostMapping("/risks")
    RiskEvaluationResponseDto riskEvaluation(@Valid @RequestBody RiskEvaluationRequestDto riskEvaluationRequest) {
        RiskEvent riskEvent = riskEvaluationMapper.toRiskEvent(riskEvaluationRequest);
        RiskEvaluationScore score = riskEvaluationUseCase.evaluate(riskEvent);
        return riskEvaluationMapper.toRiskEvaluationResponse(score);
    }

}
