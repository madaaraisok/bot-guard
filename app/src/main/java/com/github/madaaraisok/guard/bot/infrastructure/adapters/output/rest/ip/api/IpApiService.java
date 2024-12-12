package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.rest.ip.api;

import com.github.madaaraisok.guard.bot.application.ports.output.IpApiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class IpApiService implements IpApiPort {

    private final IpApiClient ipApiClient;

    public IpApiResponse getIp(String ipAddress) {
        return ipApiClient.ip(ipAddress);
    }

}
