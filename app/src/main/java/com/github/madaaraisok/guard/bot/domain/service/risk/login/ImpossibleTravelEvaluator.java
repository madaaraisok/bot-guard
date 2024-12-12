package com.github.madaaraisok.guard.bot.domain.service.risk.login;

import com.github.madaaraisok.guard.bot.application.ports.output.RiskEventOutputPort;
import com.github.madaaraisok.guard.bot.domain.model.common.Ip;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskScore;
import com.github.madaaraisok.guard.bot.domain.service.HaversineDistance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
class ImpossibleTravelEvaluator implements LoginRiskEvaluator {

    private final HaversineDistance haversineDistance;
    private final RiskEventOutputPort riskEventOutputPort;

    @Override
    public RiskScore evaluate(RiskEvent currentEvent) {
        var userId = currentEvent.getUser().id();
        return riskEventOutputPort.findPrevious(userId)
                                  .map(previousEvent -> calculateScore(currentEvent, previousEvent))
                                  .orElse(RiskScore.LOW);
    }

    private RiskScore calculateScore(RiskEvent currentEvent, RiskEvent previousEvent) {
        var distance = calculateDistance(currentEvent.getIp(), previousEvent.getIp());
        var speed = calculateSpeed(distance, currentEvent.getCreatedDate(), previousEvent.getCreatedDate());

        log.debug("Distance: {} km, speed: {} km/h", distance, speed);

        if (speed > 1000) {
            return RiskScore.HIGH;
        }

        return new RiskScore(Math.min(speed / 1000, 1.0));
    }

    private double calculateSpeed(double distance, Date currentEventCreatedDate, Date previousEventCreatedDate) {
        var startTime = currentEventCreatedDate.toInstant();
        var endTime = previousEventCreatedDate.toInstant();
        var durationInSeconds = Long.max(Duration.between(startTime, endTime).toSeconds(), 1);

        return (distance / durationInSeconds) * 3600;
    }

    private double calculateDistance(Ip currentEventIp, Ip previousEventIp) {
        return haversineDistance.calculate(
            currentEventIp.coordinates().lat(),
            currentEventIp.coordinates().lon(),
            previousEventIp.coordinates().lat(),
            previousEventIp.coordinates().lon()
        );
    }

}
