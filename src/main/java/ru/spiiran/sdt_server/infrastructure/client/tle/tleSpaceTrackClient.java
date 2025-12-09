package ru.spiiran.sdt_server.infrastructure.client.tle;

import ru.spiiran.sdt_server.infrastructure.exception.tle.NotFoundSatelliteSpaceTrackException;

public interface TLESpaceTrackClient {
    String searchByNoradId(Long noradId) throws NotFoundSatelliteSpaceTrackException;
}
