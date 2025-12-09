package ru.spiiran.sdt_server.infrastructure.database;

import ru.spiiran.sdt_server.infrastructure.dto.DtoExtremePointsOfRegion;

public interface DatabaseAccess {
    DtoExtremePointsOfRegion extremePointsOfRegion(String coverage);
}
