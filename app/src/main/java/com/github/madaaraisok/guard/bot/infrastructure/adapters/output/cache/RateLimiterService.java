package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.cache;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BandwidthBuilder;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RateLimiterService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Value("${infrastructure.adapters.output.cache.rate-limiter.ip.capacity}")
    private final long capacity;

    @Value("${infrastructure.adapters.output.cache.rate-limiter.ip.refill.tokens}")
    private final long tokens;

    @Value("${infrastructure.adapters.output.cache.rate-limiter.ip.refill.period}")
    private final Duration period;

    public Long consumeAndReturnRemaining(String ip) {
        return resolveBucket(ip).tryConsumeAndReturnRemaining(1).getRemainingTokens();
    }

    private Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, this::newBucket);
    }

    private Bucket newBucket(String ip) {
        return Bucket.builder()
                     .addLimit(newBandwidth())
                     .build();
    }

    private Bandwidth newBandwidth() {
        return BandwidthBuilder.builder()
                               .capacity(capacity)
                               .refillGreedy(tokens, period)
                               .build();
    }

}
