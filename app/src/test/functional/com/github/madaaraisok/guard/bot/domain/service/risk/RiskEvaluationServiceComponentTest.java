package com.github.madaaraisok.guard.bot.domain.service.risk;

import com.github.madaaraisok.guard.bot.application.ports.input.RiskEvaluationUseCase;
import com.github.madaaraisok.guard.bot.application.ports.output.IpApiPort;
import com.github.madaaraisok.guard.bot.application.ports.output.RiskEventOutputPort;
import com.github.madaaraisok.guard.bot.application.ports.output.UserOutputPort;
import com.github.madaaraisok.guard.bot.domain.model.common.*;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskContext;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.service.HaversineDistance;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.rest.ip.api.IpApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class RiskEvaluationServiceComponentTest {

    private static final String IP_VALUE = "192.168.0.1";
    private static final String EMAIL = "user@example.com";
    private static final User USER = new User(UUID.randomUUID(), EMAIL);

    @MockitoBean
    private UserOutputPort userOutputPort;

    @MockitoBean
    private RiskEventOutputPort riskEventOutputPort;

    @MockitoBean
    private IpApiPort ipApiPort;

    @Autowired
    private RiskEvaluationUseCase underTest;

    @Test
    void shouldEvaluateHighRiskForImpossibleTravel() {
        // given
        var riskEvent = prepareRiskEvent();

        given(ipApiPort.getIp(IP_VALUE)).willReturn(prepareIpApiResponseWithDifferentLocation());
        given(userOutputPort.findByEmail(EMAIL)).willReturn(Optional.of(USER));
        given(riskEventOutputPort.save(any())).willAnswer(this::getRiskEvent);
        given(riskEventOutputPort.findPrevious(USER.id())).willReturn(preparePreviousRiskEvent());

        // when
        var result = underTest.evaluate(riskEvent);

        // then
        assertThat(result).isNotNull()
                          .extracting(RiskEvaluationScore::risk).isEqualTo(1.);
    }

    @Test
    void shouldEvaluateLowRiskWhenLocationIsSameTwice() {
        // given
        var riskEvent = prepareRiskEvent();

        given(ipApiPort.getIp(IP_VALUE)).willReturn(prepareIpApiResponseWithSameLocation());
        given(userOutputPort.findByEmail(EMAIL)).willReturn(Optional.of(USER));
        given(riskEventOutputPort.save(any())).willAnswer(this::getRiskEvent);
        given(riskEventOutputPort.findPrevious(USER.id())).willReturn(preparePreviousRiskEvent());

        // when
        var result = underTest.evaluate(riskEvent);

        // then
        assertThat(result).isNotNull()
                          .extracting(RiskEvaluationScore::risk).isEqualTo(0.);
    }

    @Configuration
    @Import(HaversineDistance.class)
    @ComponentScan("com.github.madaaraisok.guard.bot.domain.service.risk")
    static class Config {

    }

    private RiskEvent getRiskEvent(InvocationOnMock invocation) {
        RiskEvent riskEvent = invocation.getArgument(0);
        riskEvent.setId(UUID.randomUUID());
        riskEvent.setCreatedDate(new Date());
        return riskEvent;
    }

    private RiskEvent prepareRiskEvent() {
        return new RiskEvent(null, EventType.LOGIN, EventStatus.SUCCEEDED, new RiskContext(IP_VALUE), null, USER, null);
    }

    private Optional<RiskEvent> preparePreviousRiskEvent() {
        return Optional.of(new RiskEvent(null, EventType.LOGIN, EventStatus.SUCCEEDED, new RiskContext(IP_VALUE), prepareIp(), USER, new Date()));
    }

    private Ip prepareIp() {
        return new Ip(IP_VALUE, prepareCoordinates());
    }

    private Coordinates prepareCoordinates() {
        return new Coordinates(54.3861, 18.5913);
    }

    private IpApiResponse prepareIpApiResponseWithDifferentLocation() {
        return new IpApiResponse(IP_VALUE, "success", 52.2297, 21.0122);
    }

    private IpApiResponse prepareIpApiResponseWithSameLocation() {
        return new IpApiResponse(IP_VALUE, "success", 54.3861, 18.5913);
    }

}