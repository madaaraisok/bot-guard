package com.github.madaaraisok.guard.bot.infrastructure.adapters.input.rest.risk;

import com.github.madaaraisok.guard.bot.application.dto.common.UserDto;
import com.github.madaaraisok.guard.bot.application.dto.risk.RiskContextDto;
import com.github.madaaraisok.guard.bot.application.dto.risk.RiskEvaluationRequestDto;
import com.github.madaaraisok.guard.bot.application.dto.risk.RiskEvaluationResponseDto;
import com.github.madaaraisok.guard.bot.domain.model.common.EventStatus;
import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RiskEvaluationMapperTest {

    private final RiskEvaluationMapper underTest = new RiskEvaluationMapperImpl();

    @Test
    void shouldMapRiskEvaluationScoreToRiskEvaluationResponse() {
        // given
        var id = UUID.randomUUID();
        var createdAt = new Date();
        var riskEvaluationScore = prepareRiskEvaluationScore(id, createdAt);

        // when
        var result = underTest.toRiskEvaluationResponse(riskEvaluationScore);

        // then
        assertThat(result).extracting(RiskEvaluationResponseDto::id,
                              RiskEvaluationResponseDto::type,
                              RiskEvaluationResponseDto::risk,
                              RiskEvaluationResponseDto::createdAt)
                          .containsExactly(id, EventType.LOGIN, 0.7, createdAt);
    }

    @Test
    void shouldMapRiskEvaluationRequestToRiskEvent() {
        // given
        var riskEvaluationRequest = prepareRiskEvaluationRequest();

        // when
        var result = underTest.toRiskEvent(riskEvaluationRequest);

        // then
        assertThat(result).extracting(riskEvent -> riskEvent.getContext().getIp(),
                              RiskEvent::getType,
                              RiskEvent::getStatus,
                              riskEvent -> riskEvent.getUser().email())
                          .containsExactly("192.168.0.1", EventType.LOGIN, EventStatus.SUCCEEDED, "user@example.com");
    }

    private RiskEvaluationRequestDto prepareRiskEvaluationRequest() {
        return new RiskEvaluationRequestDto(new RiskContextDto("192.168.0.1", List.of()), EventType.LOGIN, EventStatus.SUCCEEDED, prepareUser());
    }

    private UserDto prepareUser() {
        return new UserDto("user@example.com");
    }

    private RiskEvaluationScore prepareRiskEvaluationScore(UUID id, Date createdAt) {
        return RiskEvaluationScore.builder()
                                  .id(id)
                                  .type(EventType.LOGIN)
                                  .risk(0.7)
                                  .createdAt(createdAt)
                                  .build();
    }

}