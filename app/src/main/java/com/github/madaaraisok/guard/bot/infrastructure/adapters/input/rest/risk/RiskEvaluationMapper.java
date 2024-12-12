package com.github.madaaraisok.guard.bot.infrastructure.adapters.input.rest.risk;

import com.github.madaaraisok.guard.bot.application.dto.risk.RiskEvaluationRequestDto;
import com.github.madaaraisok.guard.bot.application.dto.risk.RiskEvaluationResponseDto;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface RiskEvaluationMapper {

    RiskEvent toRiskEvent(RiskEvaluationRequestDto riskEvaluationRequest);

    RiskEvaluationResponseDto toRiskEvaluationResponse(RiskEvaluationScore riskEvaluationScore);

}
