package ru.spiiran.sdt_server.infrastructure.pro42;

import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.infrastructure.dto.DtoPro42Coordinate;
import ru.spiiran.sdt_server.infrastructure.dto.DtoPro42Response;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import ru.spiiran.sdt_server.util.stream.StreamUtils;

@Component
public class Pro42ServiceImpl implements Pro42Service {
    private final Pro42 pro42;

    public Pro42ServiceImpl(Pro42 pro42) {
        this.pro42 = pro42;
    }

    @Override
    public List<DtoPro42Response> pro42(List<DtoVBResponse> satellites) {
        return satellites
                .stream()
                .map(
                        StreamUtils.wrapException(this::processModellingSatellite)
                )
                .collect(Collectors.toList());
    }

    private DtoPro42Response processModellingSatellite(DtoVBResponse satellite)
            throws IOException, InterruptedException, InvocationTargetException, IllegalAccessException
    {
        String tle = satellite.getTle();
        Long satelliteId = satellite.getSatelliteId();
        List<DtoPro42Coordinate> coordinatesForSatellite = pro42.connectTo(satelliteId, tle);
        return new DtoPro42Response(coordinatesForSatellite, satelliteId);
    }
}
