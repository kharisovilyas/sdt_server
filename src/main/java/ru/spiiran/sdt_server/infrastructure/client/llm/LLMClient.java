package ru.spiiran.sdt_server.infrastructure.client.llm;

import ru.spiiran.sdt_server.application.dto.llm.DtoLLMFilterResponse;
import ru.spiiran.sdt_server.infrastructure.exception.BadRequestException;
import ru.spiiran.sdt_server.infrastructure.exception.llm.*;

public interface LLMClient {
    DtoLLMFilterResponse llmRequest(String prompt)
            throws AuthenticationLLMException, BadRequestException, ConnectionLLMException,
            ServerErrorLLMException, TimeoutLLMException, BadResponseLLMException;
}
