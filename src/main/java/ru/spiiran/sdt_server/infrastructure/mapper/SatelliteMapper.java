package ru.spiiran.sdt_server.infrastructure.mapper;

import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.infrastructure.entity.postgres.SatelliteEntity;

import java.util.List;

public interface SatelliteMapper {
    DtoVBResponse toDtoLLMResponse(SatelliteEntity satelliteEntity);
    List<DtoVBResponse> toDtoLLMResponseList(List<SatelliteEntity> entities);
}
