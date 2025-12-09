package ru.spiiran.sdt_server.infrastructure.client.pro42;

import ru.spiiran.sdt_server.infrastructure.dto.DtoPro42Coordinate;

import java.io.IOException;
import java.util.List;

public interface RunPro42 {
    List<DtoPro42Coordinate> copyResponsePro42() throws IOException, InterruptedException;
    void init(String username) throws IOException, InterruptedException;
}
