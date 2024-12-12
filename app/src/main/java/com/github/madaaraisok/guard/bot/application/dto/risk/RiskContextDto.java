package com.github.madaaraisok.guard.bot.application.dto.risk;

import com.github.madaaraisok.guard.bot.application.dto.common.HeaderDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RiskContextDto(

    @NotEmpty
    @Schema(description = "The IP address of the originating request.")
    String ip,

    @Schema(description = "The headers of the originating request.")
    List<HeaderDto> headers
) {

}
