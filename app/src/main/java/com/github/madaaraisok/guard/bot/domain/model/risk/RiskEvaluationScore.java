package com.github.madaaraisok.guard.bot.domain.model.risk;

import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public record RiskEvaluationScore(

    UUID id,
    EventType type,
    Double risk,
    Date createdAt
) {

}
