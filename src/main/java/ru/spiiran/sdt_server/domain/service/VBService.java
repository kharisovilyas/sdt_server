package ru.spiiran.sdt_server.domain.service;

import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.infrastructure.exception.BadRequestException;
import ru.spiiran.sdt_server.infrastructure.exception.llm.*;

import java.util.List;

public interface VBService {
    List<DtoVBResponse> virtualBallisticsRequest(String prompt)
            throws AuthenticationLLMException, BadRequestException, ConnectionLLMException,
            ServerErrorLLMException, TimeoutLLMException, BadResponseLLMException;
}
