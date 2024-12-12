package com.github.madaaraisok.guard.bot.application.dto.filter;

import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public record FilterEvaluationResponseDto(

    @Schema(description = "Event ID")
    UUID id,

    @Schema(description = "Event type")
    EventType type,

    @Schema(description = "Calculated Filter Score.", minimum = "0", maximum = "1", example = "0.5")
    Double risk,

    @Schema(description = "Event timestamp")
    Date createdAt
) {

}
