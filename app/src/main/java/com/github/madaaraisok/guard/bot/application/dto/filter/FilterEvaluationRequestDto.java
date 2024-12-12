package com.github.madaaraisok.guard.bot.application.dto.filter;

import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FilterEvaluationRequestDto(

    @NotNull
    @Schema(description = "Event context")
    FilterContextDto context,

    @NotNull
    @Schema(description = "Event type")
    EventType type

) {

}
