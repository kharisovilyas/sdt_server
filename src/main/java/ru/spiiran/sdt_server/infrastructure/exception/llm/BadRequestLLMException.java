package ru.spiiran.sdt_server.infrastructure.exception.llm;

import ru.spiiran.sdt_server.infrastructure.exception.BadRequestException;

public class BadRequestLLMException extends BadRequestException {
    public BadRequestLLMException(String message) {
        super(message);
    }
}
