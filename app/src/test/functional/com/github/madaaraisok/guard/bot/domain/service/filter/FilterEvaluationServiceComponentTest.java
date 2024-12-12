package com.github.madaaraisok.guard.bot.domain.service.filter;

import com.github.madaaraisok.guard.bot.application.ports.input.FilterEvaluationUseCase;
import com.github.madaaraisok.guard.bot.application.ports.output.IpApiPort;
import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterContext;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvent;
import com.github.madaaraisok.guard.bot.domain.service.DateService;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.cache.RateLimiterService;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.rest.ip.api.IpApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
    "infrastructure.adapters.output.cache.rate-limiter.ip.capacity=10",
    "infrastructure.adapters.output.cache.rate-limiter.ip.refill.tokens=10",
    "infrastructure.adapters.output.cache.rate-limiter.ip.refill.period=PT1M",
})
class FilterEvaluationServiceComponentTest {

    private static final String IP_VALUE_1 = "192.168.0.1";
    private static final String IP_VALUE_2 = "192.168.0.2";

    @MockitoBean
    private DateService dateService;

    @MockitoBean
    private IpApiPort ipApiPort;

    @Autowired
    private FilterEvaluationUseCase underTest;

    @Test
    void shouldEvaluateLowRisk() {
        // given
        var filterEvent = prepareFilterEvent(IP_VALUE_1);
        var currentDate = new Date();

        given(ipApiPort.getIp(IP_VALUE_1)).willReturn(prepareIpApiResponse(IP_VALUE_1));
        given(dateService.currentDate()).willReturn(currentDate);

        // when
        var result = underTest.evaluate(filterEvent);

        // then
        assertThat(result).isNotNull()
                          .extracting(FilterEvaluationScore::risk).isEqualTo(0.1);
    }

    @Test
    void shouldEvaluateHighRiskWhenRateLimitExceeded() {
        // given
        var filterEvent = prepareFilterEvent(IP_VALUE_2);
        var currentDate = new Date();

        given(ipApiPort.getIp(IP_VALUE_2)).willReturn(prepareIpApiResponse(IP_VALUE_2));
        given(dateService.currentDate()).willReturn(currentDate);

        for (int i = 0; i < 10; i++) {
            underTest.evaluate(filterEvent);
        }

        // when
        var result = underTest.evaluate(filterEvent);

        // then
        assertThat(result).isNotNull()
                          .extracting(FilterEvaluationScore::risk).isEqualTo(1.);
    }

    @Configuration
    @Import(RateLimiterService.class)
    @ComponentScan("com.github.madaaraisok.guard.bot.domain.service.filter")
    static class Config {

        @Bean
        ConversionService conversionService() {
            DefaultFormattingConversionService cs = new DefaultFormattingConversionService();
            new DateTimeFormatterRegistrar().registerFormatters(cs);
            return cs;
        }

    }

    private FilterEvent prepareFilterEvent(String ip) {
        return FilterEvent.builder()
                          .context(FilterContext.builder().ip(ip).build())
                          .type(EventType.LOGIN)
                          .build();
    }

    private IpApiResponse prepareIpApiResponse(String ipValue) {
        return new IpApiResponse(ipValue, "success", 52.2297, 21.0122);
    }

}