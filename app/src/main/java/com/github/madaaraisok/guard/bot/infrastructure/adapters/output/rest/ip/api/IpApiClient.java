package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.rest.ip.api;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

interface IpApiClient {

    @CircuitBreaker(name = "ip-api-circuit-breaker")
    @GetExchange("/json/{ip}")
    IpApiResponse ip(@PathVariable String ip);

}
