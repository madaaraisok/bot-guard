package com.github.madaaraisok.guard.bot.domain.service.filter;

import com.github.madaaraisok.guard.bot.domain.model.filter.FilterContext;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvent;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.cache.RateLimiterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BotBehaviourFilterEvaluatorTest {

    @Mock
    private RateLimiterService rateLimiterService;

    private BotBehaviourFilterEvaluator filterEvaluator;

    @BeforeEach
    void setUp() {
        filterEvaluator = new BotBehaviourFilterEvaluator(rateLimiterService, 100);
    }

    @ParameterizedTest
    @CsvSource({
        "100, 0.00",
        "99, 0.01",
        "50, 0.50",
        "0, 1.00"
    })
    void shouldCalculateCorrectFilterScore(long remaining, double expectedRisk) {
        // given
        var ip = "192.168.0.1";
        var filterEvent = FilterEvent.builder()
                                     .context(new FilterContext(ip))
                                     .build();

        given(rateLimiterService.consumeAndReturnRemaining(ip)).willReturn(remaining);

        // when
        var filterScore = filterEvaluator.evaluate(filterEvent);

        // then
        assertThat(filterScore.risk()).isEqualTo(expectedRisk);
    }

}