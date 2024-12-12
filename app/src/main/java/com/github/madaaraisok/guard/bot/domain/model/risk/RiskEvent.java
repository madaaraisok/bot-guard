package com.github.madaaraisok.guard.bot.domain.model.risk;

import com.github.madaaraisok.guard.bot.domain.model.common.EventStatus;
import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import com.github.madaaraisok.guard.bot.domain.model.common.Ip;
import com.github.madaaraisok.guard.bot.domain.model.common.User;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RiskEvent {

    private UUID id;
    private EventType type;
    private EventStatus status;
    private RiskContext context;
    private Ip ip;
    private User user;
    private Date createdDate;

}
