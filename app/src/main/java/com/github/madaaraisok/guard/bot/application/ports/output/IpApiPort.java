package com.github.madaaraisok.guard.bot.application.ports.output;

import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.rest.ip.api.IpApiResponse;

public interface IpApiPort {

    IpApiResponse getIp(String ipAddress);

}
