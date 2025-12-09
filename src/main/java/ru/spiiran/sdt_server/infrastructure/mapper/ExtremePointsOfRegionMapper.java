package ru.spiiran.sdt_server.infrastructure.mapper;

import ru.spiiran.sdt_server.infrastructure.dto.DtoExtremePointsOfRegion;
import ru.spiiran.sdt_server.infrastructure.entity.postgres.ExtremePointsOfRegion;

public interface ExtremePointsOfRegionMapper {
    DtoExtremePointsOfRegion entityToDto(ExtremePointsOfRegion extremePointsOfRegion);
}
