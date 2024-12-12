package com.github.madaaraisok.guard.bot.domain.service.risk.registration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
class TemporaryEmailConfig {

    @Bean
    Set<String> blacklistedDomains(@Value("classpath:blacklistedDomains.txt") Resource domainFile) throws IOException {
        try (var lines = Files.lines(domainFile.getFile().toPath())) {
            return lines.collect(Collectors.toSet());
        }
    }

}
