package com.github.madaaraisok.guard.bot.application.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record HeaderDto(

    @NotNull
    @Schema(description = "Name of the HTTP header", example = "User-Agent")
    String name,

    @NotEmpty
    @Schema(description = "Values of the HTTP header",
        example = """
            [
                "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:15.0) Gecko/20100101 Firefox/15.0.1"
            ]
            """
    )
    List<String> values
) {

}
