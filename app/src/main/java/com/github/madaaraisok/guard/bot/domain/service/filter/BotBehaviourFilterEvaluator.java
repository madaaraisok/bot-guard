package com.github.madaaraisok.guard.bot.domain.service.filter;

import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvent;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterScore;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.cache.RateLimiterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Component
@RequiredArgsConstructor
class BotBehaviourFilterEvaluator implements FilterEvaluator {

    private final RateLimiterService rateLimiterService;

    @Value("${infrastructure.adapters.output.cache.rate-limiter.ip.capacity}")
    private final long capacity;

    @Override
    public FilterScore evaluate(FilterEvent filterEvent) {
        var remainingTokens = rateLimiterService.consumeAndReturnRemaining(filterEvent.getContext().getIp());
        var percentage = calculateRemainingTokenPercentage(remainingTokens);
        return new FilterScore(BigDecimal.ONE.subtract(percentage).doubleValue());
    }

    private BigDecimal calculateRemainingTokenPercentage(Long remainingTokens) {
        return BigDecimal.valueOf(remainingTokens)
                         .divide(BigDecimal.valueOf(capacity), 2, RoundingMode.HALF_DOWN);
    }

}
