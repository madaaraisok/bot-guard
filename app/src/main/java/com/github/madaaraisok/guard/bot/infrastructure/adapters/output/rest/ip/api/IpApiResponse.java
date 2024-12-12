package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.rest.ip.api;

public record IpApiResponse(
        String query,
        String status,
        Double lat,
        Double lon
) {

}
