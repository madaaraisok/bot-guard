package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.cache;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class RateLimiterServiceTest {

    @Test
    void shouldCacheBucket() {
        // given
        var ip = "192.168.0.1";
        var underTest = new RateLimiterService(100, 1, Duration.ofMinutes(1));

        // when
        var bucket1 = underTest.consumeAndReturnRemaining(ip);
        var bucket2 = underTest.consumeAndReturnRemaining(ip);
        var bucket3 = underTest.consumeAndReturnRemaining(ip);

        // then
        assertThat(bucket1).isEqualTo(99L);
        assertThat(bucket2).isEqualTo(98L);
        assertThat(bucket3).isEqualTo(97L);
    }

}