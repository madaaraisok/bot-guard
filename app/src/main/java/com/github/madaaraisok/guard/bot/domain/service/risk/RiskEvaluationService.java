package com.github.madaaraisok.guard.bot.domain.service.risk;

import com.github.madaaraisok.guard.bot.application.ports.input.RiskEvaluationUseCase;
import com.github.madaaraisok.guard.bot.application.ports.output.IpApiPort;
import com.github.madaaraisok.guard.bot.application.ports.output.RiskEventOutputPort;
import com.github.madaaraisok.guard.bot.application.ports.output.UserOutputPort;
import com.github.madaaraisok.guard.bot.domain.model.common.Coordinates;
import com.github.madaaraisok.guard.bot.domain.model.common.Ip;
import com.github.madaaraisok.guard.bot.domain.model.common.User;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvaluationScore;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RiskEvaluationService implements RiskEvaluationUseCase {

    private final IpApiPort ipApiPort;
    private final UserOutputPort userOutputPort;
    private final RiskEventOutputPort riskEventOutputPort;
    private final RiskEvaluatorStrategy riskEvaluatorStrategy;

    @Override
    public RiskEvaluationScore evaluate(RiskEvent riskEvent) {

        riskEvent.setUser(getUser(riskEvent.getUser()));
        riskEvent.setIp(fetchIpDetails(riskEvent.getContext().getIp()));
        riskEvent = riskEventOutputPort.save(riskEvent);

        var riskScore = riskEvaluatorStrategy.evaluate(riskEvent);

        return RiskEvaluationScore.builder()
                                  .id(riskEvent.getId())
                                  .type(riskEvent.getType())
                                  .risk(riskScore.risk())
                                  .createdAt(riskEvent.getCreatedDate())
                                  .build();
    }

    private User getUser(User user) {
        return userOutputPort.findByEmail(user.email())
                             .orElseGet(() -> userOutputPort.save(user));
    }

    private Ip fetchIpDetails(String ip) {
        var ipApiResponse = ipApiPort.getIp(ip);
        return new Ip(ipApiResponse.query(), new Coordinates(ipApiResponse.lat(), ipApiResponse.lon()));
    }

}
