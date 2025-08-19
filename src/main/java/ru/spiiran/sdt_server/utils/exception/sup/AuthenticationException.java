package ru.spiiran.sdt_server.utils.exception.sup;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
