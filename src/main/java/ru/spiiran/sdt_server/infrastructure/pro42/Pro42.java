package ru.spiiran.sdt_server.infrastructure.pro42;

import ru.spiiran.sdt_server.infrastructure.dto.DtoPro42Coordinate;

import java.io.IOException;
import java.util.List;

public interface Pro42 {
    List<DtoPro42Coordinate> connectTo(Long satelliteId, String tle) throws IOException, InterruptedException;
}
