package ru.spiiran.sdt_server.infrastructure.repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.spiiran.sdt_server.infrastructure.entity.postgres.SatelliteEntity;

public interface SatelliteRepository extends JpaRepository<SatelliteEntity, Long>,
        JpaSpecificationExecutor<SatelliteEntity> {
}

