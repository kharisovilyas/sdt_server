package ru.spiiran.sdt_server.infrastructure.exception.tle;

import ru.spiiran.sdt_server.infrastructure.exception.AuthenticationException;

public class AuthenticationSpaceTrackException extends AuthenticationException {
    public AuthenticationSpaceTrackException(String message) {
        super(message);
    }
}
