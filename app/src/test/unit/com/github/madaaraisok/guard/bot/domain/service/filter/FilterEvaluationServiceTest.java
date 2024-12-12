package com.github.madaaraisok.guard.bot.domain.service.filter;

import com.github.madaaraisok.guard.bot.application.ports.output.IpApiPort;
import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterContext;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvent;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterScore;
import com.github.madaaraisok.guard.bot.domain.service.DateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FilterEvaluationServiceTest {

    @Mock
    private IpApiPort ipApiPort;

    @Mock
    private FilterEvaluator filterEvaluator;

    @Mock
    private DateService dateService;

    @InjectMocks
    private FilterEvaluationService underTest;

    @Test
    void shouldEvaluateFilterEvent() {
        // given
        var ip = "192.168.0.1";
        var filterEvent = prepareFilterEvent(ip);
        var filterScore = new FilterScore(0.7);
        var currentDate = new Date();

        given(dateService.currentDate()).willReturn(currentDate);
        given(filterEvaluator.evaluate(filterEvent)).willReturn(filterScore);

        // when
        var result = underTest.evaluate(filterEvent);

        // then
        assertThat(result).isEqualTo(expectedFilterEvaluationScore(filterEvent, currentDate));
    }

    private FilterEvent prepareFilterEvent(String ip) {
        return FilterEvent.builder()
                          .context(FilterContext.builder().ip(ip).build())
                          .type(EventType.LOGIN)
                          .build();
    }

    private FilterEvaluationScore expectedFilterEvaluationScore(FilterEvent filterEvent, Date currentDate) {
        return FilterEvaluationScore.builder()
                                    .id(filterEvent.getId())
                                    .type(EventType.LOGIN)
                                    .risk(0.7)
                                    .createdAt(currentDate)
                                    .build();
    }

}