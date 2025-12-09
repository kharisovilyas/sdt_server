package ru.spiiran.sdt_server.infrastructure.mapper;

import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.infrastructure.dto.DtoExtremePointsOfRegion;
import ru.spiiran.sdt_server.infrastructure.entity.postgres.ExtremePointsOfRegion;
import ru.spiiran.sdt_server.infrastructure.exception.mapper.ExtremePointsMappingException;

@Component
public class ExtremePointsOfRegionMapperImpl implements ExtremePointsOfRegionMapper {
    @Override
    public DtoExtremePointsOfRegion entityToDto(ExtremePointsOfRegion extremePointsOfRegion) throws ExtremePointsMappingException {
        return new DtoExtremePointsOfRegion(safeThrowAnError(extremePointsOfRegion));
    }

    private <T> T safeThrowAnError(T t) throws RuntimeException {
        if(t == null) {
            throw new ExtremePointsMappingException("Ошибка mapping: объект ExtremePointsOfRegion равен null");
        } else {
            return t;
        }
    }
}
