package ru.spiiran.sdt_server.infrastructure.exception.tle;

import ru.spiiran.sdt_server.infrastructure.exception.NotFoundException;

public class NotFoundSatelliteSpaceTrackException extends NotFoundException {
  public NotFoundSatelliteSpaceTrackException(String message) {
    super(message);
  }
}
