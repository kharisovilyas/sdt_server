package ru.spiiran.sdt_server.infrastructure.exception.tle;

import ru.spiiran.sdt_server.infrastructure.exception.RequestException;

public class RequestSpaceTrackException extends RequestException {
  public RequestSpaceTrackException(String message) {
    super(message);
  }
}
