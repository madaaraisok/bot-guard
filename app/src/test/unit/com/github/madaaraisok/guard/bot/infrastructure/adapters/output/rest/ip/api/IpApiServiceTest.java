package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.rest.ip.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class IpApiServiceTest {

    @Mock
    private IpApiClient ipApiClient;

    @InjectMocks
    private IpApiService underTest;

    @Test
    void shouldGetIp() {
        // given
        var ipAddress = "192.168.0.1";
        var ipApiResponse = mock(IpApiResponse.class);

        given(ipApiClient.ip(ipAddress)).willReturn(ipApiResponse);

        // when
        var result = underTest.getIp(ipAddress);

        // then
        assertThat(result).isEqualTo(ipApiResponse);
    }

}