package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk;

import com.github.madaaraisok.guard.bot.domain.model.common.User;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk.entity.RiskEventEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RiskEventPersistenceAdapterTest {

    @Mock
    private RiskEventRepository riskEventRepository;

    @Mock
    private RiskEventPersistenceMapper riskEventPersistenceMapper;

    @InjectMocks
    private RiskEventPersistenceAdapter underTest;

    @Test
    void shouldSaveRiskEvent() {
        // given
        var riskEvent = mock(RiskEvent.class);
        var riskEventEntity = mock(RiskEventEntity.class);

        given(riskEvent.getUser()).willReturn(new User(UUID.randomUUID(), "user@example.com"));
        given(riskEventPersistenceMapper.toRiskEventEntity(riskEvent)).willReturn(riskEventEntity);
        given(riskEventRepository.save(riskEventEntity)).willReturn(riskEventEntity);
        given(riskEventPersistenceMapper.toRiskEvent(riskEventEntity)).willReturn(riskEvent);

        // when
        var result = underTest.save(riskEvent);

        // then
        assertThat(result).isEqualTo(riskEvent);
    }

    @Test
    void shouldFindPreviousEvent() {
        // given
        var userId = UUID.randomUUID();
        var riskEvent = mock(RiskEvent.class);
        var riskEventEntity = mock(RiskEventEntity.class);

        given(riskEventRepository.findPreviousByUserId(userId)).willReturn(Optional.of(riskEventEntity));
        given(riskEventPersistenceMapper.toRiskEvent(riskEventEntity)).willReturn(riskEvent);

        // when
        var result = underTest.findPrevious(userId);

        // then
        assertThat(result).isPresent()
                          .contains(riskEvent);
    }

}