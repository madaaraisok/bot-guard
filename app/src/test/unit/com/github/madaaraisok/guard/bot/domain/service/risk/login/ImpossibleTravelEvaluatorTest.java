package com.github.madaaraisok.guard.bot.domain.service.risk.login;

import com.github.madaaraisok.guard.bot.application.ports.output.RiskEventOutputPort;
import com.github.madaaraisok.guard.bot.domain.model.common.Coordinates;
import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import com.github.madaaraisok.guard.bot.domain.model.common.Ip;
import com.github.madaaraisok.guard.bot.domain.model.common.User;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskContext;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskScore;
import com.github.madaaraisok.guard.bot.domain.service.HaversineDistance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ImpossibleTravelEvaluatorTest {

    @Mock
    private HaversineDistance haversineDistance;

    @Mock
    private RiskEventOutputPort riskEventOutputPort;

    @InjectMocks
    private ImpossibleTravelEvaluator underTest;

    @Test
    void shouldReturnLowRiskWhenNoPreviousEvent() {
        // given
        var createdDate = Instant.now();
        var currentEvent = createRiskEvent(1.0, 2.0, createdDate);

        given(riskEventOutputPort.findPrevious(any())).willReturn(Optional.empty());

        // when
        var result = underTest.evaluate(currentEvent);

        // then
        assertThat(result).isEqualTo(RiskScore.LOW);
    }

    @Test
    void shouldReturnHighRiskForImpossibleTravel() {
        // given
        var createdDate = Instant.now();
        var currentEvent = createRiskEvent(1.0, 2.0, createdDate);
        var previousEvent = createRiskEvent(3.0, 4.0, createdDate);

        given(riskEventOutputPort.findPrevious(any())).willReturn(Optional.of(previousEvent));
        given(haversineDistance.calculate(
            currentEvent.getIp().coordinates().lat(),
            currentEvent.getIp().coordinates().lon(),
            previousEvent.getIp().coordinates().lat(),
            previousEvent.getIp().coordinates().lon()
        )).willReturn(10000.0);

        // when
        var result = underTest.evaluate(currentEvent);

        // then
        assertThat(result).isEqualTo(RiskScore.HIGH);
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0, 0.0", // 0 km/h

        "1, 0, 1.0", // 3600 km/h

        "100, 10, 0.6", // 600 km/h
        "200, 10, 1.0", // 600 km/h

        "100, 30, 0.2", // 200 km/h
        "200, 30, 0.4", // 400 km/h
        "300, 30, 0.6", // 600 km/h
        "400, 30, 0.8", // 600 km/h
        "500, 30, 1.0", // 600 km/h

        "100, 60, 0.1", // 100 km/h
        "200, 60, 0.2", // 200 km/h
        "300, 60, 0.3", // 300 km/h
        "400, 60, 0.4", // 400 km/h
        "500, 60, 0.5", // 500 km/h
        "600, 60, 0.6", // 600 km/h
        "700, 60, 0.7", // 700 km/h
        "800, 60, 0.8", // 800 km/h
        "900, 60, 0.9", // 900 km/h
        "1000, 60, 1.0", // 1000 km/h
        "10000, 60, 1.0", // 10000 km/h
        "100000, 60, 1.0", // 100000 km/h
        "1000000, 60, 1.0", // 1000000 km/h
    })
    void shouldReturnCalculatedRiskForPossibleTravel(double distance, long minutesDifference, double expectedRisk) {
        // given
        var createdDate = Instant.now();
        var currentEvent = createRiskEvent(1.0, 2.0, createdDate);
        var previousEvent = createRiskEvent(3.0, 4.0, createdDate.plus(minutesDifference, ChronoUnit.MINUTES));

        given(riskEventOutputPort.findPrevious(any())).willReturn(Optional.of(previousEvent));
        given(haversineDistance.calculate(
            currentEvent.getIp().coordinates().lat(),
            currentEvent.getIp().coordinates().lon(),
            previousEvent.getIp().coordinates().lat(),
            previousEvent.getIp().coordinates().lon()
        )).willReturn(distance);

        // when
        var result = underTest.evaluate(currentEvent);

        // then
        assertThat(result).isEqualTo(new RiskScore(expectedRisk));
    }

    private RiskEvent createRiskEvent(double lat, double lon, Instant createdDate) {
        var ip = new Ip("ip", new Coordinates(lat, lon));
        var user = new User(UUID.randomUUID(), "test@example.com");
        return new RiskEvent(UUID.randomUUID(), EventType.LOGIN, null, new RiskContext(), ip, user, Date.from(createdDate));
    }

}