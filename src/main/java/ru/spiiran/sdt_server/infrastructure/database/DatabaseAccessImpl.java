package ru.spiiran.sdt_server.infrastructure.database;

import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.infrastructure.dto.DtoExtremePointsOfRegion;
import ru.spiiran.sdt_server.infrastructure.entity.postgres.ExtremePointsOfRegion;
import ru.spiiran.sdt_server.infrastructure.exception.mapper.ExtremePointsMappingException;
import ru.spiiran.sdt_server.infrastructure.mapper.ExtremePointsOfRegionMapper;
import ru.spiiran.sdt_server.infrastructure.repository.postgres.ExtremePointsOfRegionRepository;

@Component
public class DatabaseAccessImpl implements DatabaseAccess {
    private final ExtremePointsOfRegionMapper extremePointsOfRegionMapper;
    private final ExtremePointsOfRegionRepository extremePointsOfRegionRepository;

    public DatabaseAccessImpl(ExtremePointsOfRegionMapper extremePointsOfRegionMapper, ExtremePointsOfRegionRepository extremePointsOfRegionRepository) {
        this.extremePointsOfRegionMapper = extremePointsOfRegionMapper;
        this.extremePointsOfRegionRepository = extremePointsOfRegionRepository;
    }

    @Override
    public DtoExtremePointsOfRegion extremePointsOfRegion(String coverage) throws ExtremePointsMappingException {
        ExtremePointsOfRegion extremePointsOfRegion = extremePointsOfRegionRepository.findByRegionName(coverage);
        return extremePointsOfRegionMapper.entityToDto(extremePointsOfRegion);
    }
}
