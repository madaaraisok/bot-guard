package com.github.madaaraisok.guard.bot.domain.service.risk.registration;

import com.github.madaaraisok.guard.bot.application.ports.output.UserOutputPort;
import com.github.madaaraisok.guard.bot.domain.model.common.User;
import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MultipleAliasesPerEmailEvaluatorTest {

    @Mock
    private UserOutputPort userOutputPort;

    @InjectMocks
    private MultipleAliasesPerEmailEvaluator underTest;

    @ParameterizedTest
    @CsvSource({
        "0, 0.0",
        "1, 0.0",
        "2, 0.5",
        "3, 0.5",
        "4, 0.5",
        "5, 1.0"
    })
    void shouldReturnRisk(long aliasCount, double expectedRisk) {
        // given
        var event = new RiskEvent();
        event.setUser(new User(UUID.randomUUID(), "user@example.com"));

        given(userOutputPort.countBySanitizedEmail(event.getUser().sanitizedEmail())).willReturn(aliasCount);

        // when
        var result = underTest.evaluate(event);

        // then
        assertThat(result.risk()).isEqualTo(expectedRisk);
    }

}