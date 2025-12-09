package ru.spiiran.sdt_server.infrastructure.exception.llm;

import ru.spiiran.sdt_server.infrastructure.exception.ServerErrorException;

public class ServerErrorLLMException extends ServerErrorException {
    public ServerErrorLLMException(String message) {
        super(message);
    }
}
