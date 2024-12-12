package com.github.madaaraisok.guard.bot.domain.model.filter;

import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterEvent {

    private UUID id;
    private EventType type;
    private FilterContext context;
    private Date createdDate;

}
