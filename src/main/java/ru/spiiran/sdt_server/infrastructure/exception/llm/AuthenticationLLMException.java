package ru.spiiran.sdt_server.infrastructure.exception.llm;

import ru.spiiran.sdt_server.infrastructure.exception.AuthenticationException;

public class AuthenticationLLMException extends AuthenticationException {
    public AuthenticationLLMException(String message) {
        super(message);
    }
}
