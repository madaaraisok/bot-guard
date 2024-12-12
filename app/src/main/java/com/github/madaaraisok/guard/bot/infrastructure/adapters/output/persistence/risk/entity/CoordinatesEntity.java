package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CoordinatesEntity {

    private double lat;
    private double lon;

}
