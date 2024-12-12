package com.github.madaaraisok.guard.bot.domain.model.risk;

public record RiskScore(Double risk) {

    public static RiskScore HIGH = new RiskScore(1.);
    public static RiskScore LOW = new RiskScore(0.);

    public static RiskScore max(RiskScore a, RiskScore b) {
        return a.risk() >= b.risk() ? a : b;
    }

}
