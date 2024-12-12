package com.github.madaaraisok.guard.bot.infrastructure.adapters.input.rest.filter;

import com.github.madaaraisok.guard.bot.application.dto.filter.FilterContextDto;
import com.github.madaaraisok.guard.bot.application.dto.filter.FilterEvaluationRequestDto;
import com.github.madaaraisok.guard.bot.application.dto.filter.FilterEvaluationResponseDto;
import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvent;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FilterEvaluationMapperTest {

    private final FilterEvaluationMapper underTest = new FilterEvaluationMapperImpl();

    @Test
    void shouldMapFilterEvaluationScoreToFilterEvaluationResponse() {
        // given
        var id = UUID.randomUUID();
        var createdAt = new Date();
        var filterEvaluationScore = prepareFilterEvaluationScore(id, createdAt);

        // when
        var result = underTest.toFilterEvaluationResponse(filterEvaluationScore);

        // then
        assertThat(result).extracting(FilterEvaluationResponseDto::id,
                              FilterEvaluationResponseDto::type,
                              FilterEvaluationResponseDto::risk,
                              FilterEvaluationResponseDto::createdAt)
                          .containsExactly(id, EventType.LOGIN, 0.7, createdAt);
    }

    @Test
    void shouldMapFilterEvaluationRequestToFilterEvent() {
        // given
        var filterEvaluationRequest = prepareFilterEvaluationRequest();

        // when
        var result = underTest.toFilterEvent(filterEvaluationRequest);

        // then
        assertThat(result).extracting(filterEvent -> filterEvent.getContext().getIp(), FilterEvent::getType)
                          .containsExactly("192.168.0.1", EventType.LOGIN);
    }

    private FilterEvaluationRequestDto prepareFilterEvaluationRequest() {
        return new FilterEvaluationRequestDto(new FilterContextDto("192.168.0.1", List.of()), EventType.LOGIN);
    }

    private FilterEvaluationScore prepareFilterEvaluationScore(UUID id, Date createdAt) {
        return FilterEvaluationScore.builder()
                                    .id(id)
                                    .type(EventType.LOGIN)
                                    .risk(0.7)
                                    .createdAt(createdAt)
                                    .build();
    }

}