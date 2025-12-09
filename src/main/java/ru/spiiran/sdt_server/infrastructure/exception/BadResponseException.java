package ru.spiiran.sdt_server.infrastructure.exception;

public class BadResponseException extends RuntimeException {
    public BadResponseException(String message) {
        super(message);
    }
}
