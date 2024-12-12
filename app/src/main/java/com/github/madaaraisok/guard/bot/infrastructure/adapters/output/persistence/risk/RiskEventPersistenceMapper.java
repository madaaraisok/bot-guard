package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk;

import com.github.madaaraisok.guard.bot.domain.model.risk.RiskEvent;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk.entity.RiskEventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface RiskEventPersistenceMapper {

    RiskEventEntity toRiskEventEntity(RiskEvent riskEvent);

    RiskEvent toRiskEvent(RiskEventEntity riskEventEntity);

}
