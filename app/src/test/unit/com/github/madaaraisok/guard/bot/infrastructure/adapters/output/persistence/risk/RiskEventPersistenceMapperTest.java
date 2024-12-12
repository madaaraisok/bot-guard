package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk;

import com.github.madaaraisok.guard.bot.domain.model.common.*;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk.entity.CoordinatesEntity;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk.entity.IpEntity;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk.entity.RiskEventEntity;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.user.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RiskEventPersistenceMapperTest {

    private final RiskEventPersistenceMapper underTest = new RiskEventPersistenceMapperImpl();

    @Test
    void shouldMapRiskEventToRiskEventEntity() {
        // given
        var id = UUID.randomUUID();
        var createdDate = new Date();
        var riskEvent = prepareRiskEvent(id, createdDate);

        // when
        var result = underTest.toRiskEventEntity(riskEvent);

        // then
        assertThat(result).extracting(RiskEventEntity::getId,
                              RiskEventEntity::getType,
                              RiskEventEntity::getStatus,
                              riskEventEntity -> riskEventEntity.getUser().getEmail(),
                              riskEventEntity -> riskEventEntity.getIp().getValue(),
                              riskEventEntity -> riskEventEntity.getIp().getCoordinates().getLat(),
                              riskEventEntity -> riskEventEntity.getIp().getCoordinates().getLon(),
                              RiskEventEntity::getCreatedDate
                          )
                          .containsExactly(id, EventType.LOGIN, EventStatus.SUCCEEDED, "user@example.com",
                              "192.168.0.1", 52.2297700, 21.0117800, createdDate);
    }

    @Test
    void shouldMapRiskEventEntityToRiskEvent() {
        // given
        var id = UUID.randomUUID();
        var createdDate = new Date();
        var riskEventEntity = prepareRiskEventEntity(id, createdDate);

        // when
        var result = underTest.toRiskEvent(riskEventEntity);

        // then
        assertThat(result).extracting(RiskEvent::getId,
                              RiskEvent::getType,
                              RiskEvent::getStatus,
                              riskEvent -> riskEvent.getUser().email(),
                              riskEvent -> riskEvent.getIp().value(),
                              riskEvent -> riskEvent.getIp().coordinates().lat(),
                              riskEvent -> riskEvent.getIp().coordinates().lon(),
                              RiskEvent::getCreatedDate
                          )
                          .containsExactly(id, EventType.LOGIN, EventStatus.SUCCEEDED, "user@example.com",
                              "192.168.0.1", 52.2297700, 21.0117800, createdDate);
    }

    private RiskEvent prepareRiskEvent(UUID id, Date createdDate) {
        return RiskEvent.builder()
                        .id(id)
                        .type(EventType.LOGIN)
                        .status(EventStatus.SUCCEEDED)
                        .ip(prepareIp())
                        .user(prepareUser())
                        .createdDate(createdDate)
                        .build();
    }

    private User prepareUser() {
        return new User(UUID.randomUUID(), "user@example.com");
    }

    private Ip prepareIp() {
        return new Ip("192.168.0.1", prepareCoordinates());
    }

    private Coordinates prepareCoordinates() {
        return new Coordinates(52.2297700, 21.0117800);
    }

    private RiskEventEntity prepareRiskEventEntity(UUID id, Date createdDate) {
        return RiskEventEntity.builder()
                              .id(id)
                              .type(EventType.LOGIN)
                              .status(EventStatus.SUCCEEDED)
                              .user(prepareUserEntity())
                              .ip(prepareIpEntity())
                              .createdDate(createdDate)
                              .build();
    }

    private UserEntity prepareUserEntity() {
        return UserEntity.builder()
                         .email("user@example.com")
                         .build();
    }

    private IpEntity prepareIpEntity() {
        return IpEntity.builder()
                       .value("192.168.0.1")
                       .coordinates(prepareCoordinatesEntity())
                       .build();
    }

    private CoordinatesEntity prepareCoordinatesEntity() {
        return CoordinatesEntity.builder()
                                .lat(52.2297700)
                                .lon(21.0117800)
                                .build();
    }

}