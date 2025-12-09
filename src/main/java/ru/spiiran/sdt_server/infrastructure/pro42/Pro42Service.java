package ru.spiiran.sdt_server.infrastructure.pro42;

import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.infrastructure.dto.DtoPro42Response;

import java.util.List;

public interface Pro42Service {
    List<DtoPro42Response> pro42(List<DtoVBResponse> satellites);
}
