package ru.spiiran.sdt_server.utils.exception.tle;

import ru.spiiran.sdt_server.utils.exception.sup.AuthenticationException;

public class AuthenticationSpaceTrackException extends AuthenticationException {
    public AuthenticationSpaceTrackException(String message) {
        super(message);
    }
}
