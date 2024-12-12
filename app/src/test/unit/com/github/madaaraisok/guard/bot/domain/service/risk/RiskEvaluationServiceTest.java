package com.github.madaaraisok.guard.bot.domain.service.risk;

import com.github.madaaraisok.guard.bot.application.ports.output.IpApiPort;
import com.github.madaaraisok.guard.bot.application.ports.output.RiskEventOutputPort;
import com.github.madaaraisok.guard.bot.application.ports.output.UserOutputPort;
import com.github.madaaraisok.guard.bot.domain.model.common.*;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskContext;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskScore;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.rest.ip.api.IpApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RiskEvaluationServiceTest {

    private static final UUID EVENT_ID = UUID.randomUUID();
    private static final String EMAIL = "test@example.com";
    private static final String IP_VALUE = "127.0.0.1";
    private static final Date CREATED_DATE = new Date();

    @Mock
    private IpApiPort ipApiPort;

    @Mock
    private UserOutputPort userOutputPort;

    @Mock
    private RiskEventOutputPort riskEventOutputPort;

    @Mock
    private RiskEvaluatorStrategy riskEvaluatorStrategy;

    @InjectMocks
    private RiskEvaluationService underTest;

    @Test
    void shouldEvaluateRisk() {
        // given
        var user = new User(UUID.randomUUID(), EMAIL);
        var ip = new Ip(IP_VALUE, new Coordinates(52.0, 13.0));
        var riskEvent = prepareRiskEvent(user);
        var ipApiResponse = prepareIpApiResponse();

        given(ipApiPort.getIp(IP_VALUE)).willReturn(ipApiResponse);
        given(userOutputPort.findByEmail(EMAIL)).willReturn(Optional.of(user));
        given(riskEventOutputPort.save(any())).willReturn(prepareSavedRiskEvent(EVENT_ID, ip, user, CREATED_DATE));
        given(riskEvaluatorStrategy.evaluate(any())).willReturn(new RiskScore(0.5));

        // when
        var result = underTest.evaluate(riskEvent);

        // then
        assertThat(result).isNotNull()
                          .extracting(RiskEvaluationScore::id,
                              RiskEvaluationScore::type,
                              RiskEvaluationScore::risk,
                              RiskEvaluationScore::createdAt)
                          .containsExactly(EVENT_ID, EventType.LOGIN, 0.5, CREATED_DATE);
    }

    @Test
    void shouldEvaluateRiskWhenUserDoesNotExist() {
        // given
        var user = new User(UUID.randomUUID(), EMAIL);
        var ip = new Ip(IP_VALUE, new Coordinates(52.0, 13.0));
        var riskEvent = prepareRiskEvent(user);
        var ipApiResponse = prepareIpApiResponse();

        given(ipApiPort.getIp(IP_VALUE)).willReturn(ipApiResponse);
        given(userOutputPort.findByEmail(EMAIL)).willReturn(Optional.empty());
        given(userOutputPort.save(user)).willReturn(user);
        given(riskEventOutputPort.save(any())).willReturn(prepareSavedRiskEvent(EVENT_ID, ip, user, CREATED_DATE));
        given(riskEvaluatorStrategy.evaluate(any())).willReturn(new RiskScore(0.5));

        // when
        var result = underTest.evaluate(riskEvent);

        // then
        assertThat(result).isNotNull()
                          .extracting(RiskEvaluationScore::id,
                              RiskEvaluationScore::type,
                              RiskEvaluationScore::risk,
                              RiskEvaluationScore::createdAt)
                          .containsExactly(EVENT_ID, EventType.LOGIN, 0.5, CREATED_DATE);
    }

    private RiskEvent prepareRiskEvent(User user) {
        return prepareSavedRiskEvent(null, null, user, null);
    }

    private RiskEvent prepareSavedRiskEvent(UUID id, Ip ip, User user, Date createdDate) {
        return new RiskEvent(id, EventType.LOGIN, EventStatus.SUCCEEDED, new RiskContext(IP_VALUE), ip, user, createdDate);
    }

    private IpApiResponse prepareIpApiResponse() {
        return new IpApiResponse(IP_VALUE, "success", 52.0, 13.0);
    }

}