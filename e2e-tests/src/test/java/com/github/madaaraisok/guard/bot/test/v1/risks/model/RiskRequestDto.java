package com.github.madaaraisok.guard.bot.test.v1.risks.model;

import com.github.madaaraisok.guard.bot.common.model.EventStatus;
import com.github.madaaraisok.guard.bot.common.model.EventType;
import com.github.madaaraisok.guard.bot.common.model.User;

public record RiskRequestDto(
        RiskContext context,
        EventType type,
        EventStatus status,
        User user
) {

}
