package ru.spiiran.sdt_server.infrastructure.mapper;

import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoModelSat;
import ru.spiiran.sdt_server.infrastructure.entity.postgres.SatelliteEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SatelliteMapperImpl implements SatelliteMapper {

    @Override
    public DtoVBResponse toDtoLLMResponse(SatelliteEntity satelliteEntity) {
        String tle = satelliteEntity.getTle();
        DtoModelSat modelSat = new DtoModelSat(satelliteEntity);
        return new DtoVBResponse(satelliteEntity.getId(), tle, modelSat);
    }

    @Override
    public List<DtoVBResponse> toDtoLLMResponseList(List<SatelliteEntity> entities) {
        return entities.stream()
                .map(this::toDtoLLMResponse)
                .collect(Collectors.toList());
    }
}
