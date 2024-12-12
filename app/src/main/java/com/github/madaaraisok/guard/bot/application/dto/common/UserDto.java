package com.github.madaaraisok.guard.bot.application.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record UserDto(

    @Email
    @Schema(description = "The identified user's email address", example = "user@domain.com")
    String email
) {

}
