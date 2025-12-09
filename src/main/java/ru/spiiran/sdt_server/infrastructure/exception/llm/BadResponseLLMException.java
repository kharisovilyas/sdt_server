package ru.spiiran.sdt_server.infrastructure.exception.llm;

import ru.spiiran.sdt_server.infrastructure.exception.BadResponseException;

public class BadResponseLLMException extends BadResponseException {
  public BadResponseLLMException(String message) {
    super(message);
  }
}
