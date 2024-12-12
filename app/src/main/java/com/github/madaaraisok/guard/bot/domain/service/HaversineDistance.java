package com.github.madaaraisok.guard.bot.domain.service;

import org.springframework.stereotype.Component;

/**
 * Haversine Formula is based on the spherical law of haversines.
 *
 * @see <a href="https://www.baeldung.com/java-find-distance-between-points">java-find-distance-between-points</a>
 */
@Component
public class HaversineDistance {

    private static final double EARTH_RADIUS = 6371;

    public double calculate(double startLat, double startLon, double endLat, double endLon) {

        double dLat = Math.toRadians((endLat - startLat));
        double dLon = Math.toRadians((endLon - startLon));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLon);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

}
