package com.github.madaaraisok.guard.bot.infrastructure.adapters.input.rest.filter;

import com.github.madaaraisok.guard.bot.application.dto.filter.FilterEvaluationRequestDto;
import com.github.madaaraisok.guard.bot.application.dto.filter.FilterEvaluationResponseDto;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FilterEvaluationMapper {

    FilterEvent toFilterEvent(FilterEvaluationRequestDto riskEvaluationRequest);

    FilterEvaluationResponseDto toFilterEvaluationResponse(FilterEvaluationScore filterEvaluationScore);

}
