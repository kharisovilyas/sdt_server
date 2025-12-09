package ru.spiiran.sdt_server.infrastructure.exception.mapper;

public class DataMappingException extends RuntimeException {
    public DataMappingException(String message) {
        super(message);
    }
    public DataMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
