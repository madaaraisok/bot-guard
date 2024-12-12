package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk;

import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.risk.entity.RiskEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface RiskEventRepository extends JpaRepository<RiskEventEntity, UUID> {

    @Query("SELECT r FROM RiskEventEntity r JOIN FETCH r.user u WHERE u.id = ?1 ORDER BY r.createdDate DESC LIMIT 1 OFFSET 1")
    Optional<RiskEventEntity> findPreviousByUserId(UUID userId);

}
