package ru.spiiran.sdt_server.domain.service;

import ru.spiiran.sdt_server.infrastructure.exception.tle.NotFoundSatelliteSpaceTrackException;

public interface TLEUploaderService {
    String searchByNoradId(Long tleId) throws NotFoundSatelliteSpaceTrackException;
}
