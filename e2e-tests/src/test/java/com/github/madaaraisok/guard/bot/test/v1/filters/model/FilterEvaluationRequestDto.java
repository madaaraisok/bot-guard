package com.github.madaaraisok.guard.bot.test.v1.filters.model;

import com.github.madaaraisok.guard.bot.common.model.EventType;

public record FilterEvaluationRequestDto(
    FilterContextDto context,

    EventType type
) {

}
