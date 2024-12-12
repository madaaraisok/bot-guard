package com.github.madaaraisok.guard.bot.application.dto.risk;

import com.github.madaaraisok.guard.bot.application.dto.common.UserDto;
import com.github.madaaraisok.guard.bot.domain.model.common.EventStatus;
import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RiskEvaluationRequestDto(

    @NotNull
    @Schema(description = "Event context")
    RiskContextDto context,

    @NotNull
    @Schema(description = "Event type")
    EventType type,

    @NotNull
    @Schema(description = "Event status")
    EventStatus status,

    @Valid
    @NotNull
    @Schema(description = "User associated with the event")
    UserDto user
) {

}
