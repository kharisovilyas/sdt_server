package ru.spiiran.sdt_server.service;

import ru.spiiran.sdt_server.utils.exception.tle.NotFoundSatelliteSpaceTrackException;

public interface tleUploaderService {
    String searchByNoradId(Long tleId) throws NotFoundSatelliteSpaceTrackException;
}
