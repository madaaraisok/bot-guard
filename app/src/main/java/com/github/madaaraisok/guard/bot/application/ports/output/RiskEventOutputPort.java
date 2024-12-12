package com.github.madaaraisok.guard.bot.application.ports.output;

import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;

import java.util.Optional;
import java.util.UUID;

public interface RiskEventOutputPort {

    RiskEvent save(RiskEvent riskEvent);

    Optional<RiskEvent> findPrevious(UUID userId);

}
