package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class IpEntity {

    @Column(name = "ip")
    private String value;

    @Embedded
    private CoordinatesEntity coordinates;

}
