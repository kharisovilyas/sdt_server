package ru.spiiran.sdt_server.infrastructure.exception;

public class TimeoutException extends RuntimeException {
    public TimeoutException(String message) {
        super(message);
    }
}
