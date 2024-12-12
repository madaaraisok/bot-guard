package com.github.madaaraisok.guard.bot.infrastructure.adapters.input.rest;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SwaggerConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI().info(info());
    }

    private Info info() {
        return new Info().title("Bot Guard")
                         .description("Bot Guard API Documentation");
    }

}
