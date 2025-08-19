package ru.spiiran.sdt_server.utils.exception.tle;

import ru.spiiran.sdt_server.utils.exception.sup.NotFoundException;

public class NotFoundSatelliteSpaceTrackException extends NotFoundException {
  public NotFoundSatelliteSpaceTrackException(String message) {
    super(message);
  }
}
