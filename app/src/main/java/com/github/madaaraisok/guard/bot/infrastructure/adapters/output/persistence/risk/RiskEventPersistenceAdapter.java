package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk;

import com.github.madaaraisok.guard.bot.application.ports.output.RiskEventOutputPort;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
class RiskEventPersistenceAdapter implements RiskEventOutputPort {

    private final RiskEventRepository riskEventRepository;
    private final RiskEventPersistenceMapper riskEventPersistenceMapper;

    public RiskEvent save(RiskEvent riskEvent) {
        log.info("Saving risk, userId: {}", riskEvent.getUser().email());
        var riskEntity = riskEventPersistenceMapper.toRiskEventEntity(riskEvent);
        riskEntity = riskEventRepository.save(riskEntity);
        return riskEventPersistenceMapper.toRiskEvent(riskEntity);
    }

    public Optional<RiskEvent> findPrevious(UUID userId) {
        log.info("Getting latest risk event, userId: {}", userId);
        return this.riskEventRepository.findPreviousByUserId(userId)
                                       .map(riskEventPersistenceMapper::toRiskEvent);
    }

}
