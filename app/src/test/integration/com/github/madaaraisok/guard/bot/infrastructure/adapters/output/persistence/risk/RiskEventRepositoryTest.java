package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk;

import com.github.madaaraisok.guard.bot.domain.model.common.EventStatus;
import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk.entity.CoordinatesEntity;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk.entity.IpEntity;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk.entity.RiskEventEntity;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.user.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@EnableJpaAuditing
@DataJpaTest
class RiskEventRepositoryTest {

    @Autowired
    private RiskEventRepository underTest;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldFindPreviousByUserId() {
        // given
        var userEntity = new UserEntity();
        var ipEntity = new IpEntity("192.168.0.1", new CoordinatesEntity(0.0, 0.0));
        userEntity.setEmail("user@example.com");
        userEntity.setSanitizedEmail("user@example.com");

        entityManager.persist(userEntity);

        var riskEvent1 = RiskEventEntity.builder()
                                        .user(userEntity)
                                        .status(EventStatus.SUCCEEDED)
                                        .type(EventType.LOGIN)
                                        .ip(ipEntity)
                                        .build();

        var riskEvent2 = RiskEventEntity.builder()
                                        .user(userEntity)
                                        .status(EventStatus.FAILED)
                                        .type(EventType.LOGIN)
                                        .ip(ipEntity)
                                        .build();

        underTest.save(riskEvent1);
        underTest.save(riskEvent2);

        // when
        var result = underTest.findPreviousByUserId(userEntity.getId());

        // then
        assertThat(result).isPresent()
                          .get()
                          .extracting("id")
                          .isEqualTo(riskEvent1.getId());
    }

    @Test
    void shouldReturnEmptyWhenNoRiskEvents() {
        // given
        var userId = UUID.randomUUID();

        // when
        var result = underTest.findPreviousByUserId(userId);

        // then
        assertThat(result).isNotPresent();
    }

}