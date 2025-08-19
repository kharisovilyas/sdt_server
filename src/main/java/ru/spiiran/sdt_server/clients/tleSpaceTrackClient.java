package ru.spiiran.sdt_server.clients;

import ru.spiiran.sdt_server.utils.exception.tle.NotFoundSatelliteSpaceTrackException;

public interface tleSpaceTrackClient {
    String searchByNoradId(Long noradId) throws NotFoundSatelliteSpaceTrackException;
}
