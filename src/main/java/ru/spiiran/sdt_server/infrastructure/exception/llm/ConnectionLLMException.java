package ru.spiiran.sdt_server.infrastructure.exception.llm;

import java.net.ConnectException;

public class ConnectionLLMException extends ConnectException {
    public ConnectionLLMException(String message) {
        super(message);
    }
}
