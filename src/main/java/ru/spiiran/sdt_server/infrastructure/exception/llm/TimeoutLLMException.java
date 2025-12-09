package ru.spiiran.sdt_server.infrastructure.exception.llm;

import ru.spiiran.sdt_server.infrastructure.exception.TimeoutException;

public class TimeoutLLMException extends TimeoutException {
    public TimeoutLLMException(String message) {
        super(message);
    }
}
