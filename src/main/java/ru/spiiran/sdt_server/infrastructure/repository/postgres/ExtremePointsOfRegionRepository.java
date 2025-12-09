package ru.spiiran.sdt_server.infrastructure.repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.spiiran.sdt_server.infrastructure.entity.postgres.ExtremePointsOfRegion;

@Repository
public interface ExtremePointsOfRegionRepository extends JpaRepository<ExtremePointsOfRegion, Long> {
    @Query("SELECT e FROM ExtremePointsOfRegion e JOIN e.region r WHERE r = :regionName")
    ExtremePointsOfRegion findByRegionName(@Param("regionName") String regionName);
}
