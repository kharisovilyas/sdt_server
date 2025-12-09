package ru.spiiran.sdt_server.application.event.response;

import ru.spiiran.sdt_server.application.dto.llm.DtoLLMFilterResponse;

public class LLMFilterResponseEvent extends AbstractLLMFilterEventResponse {

    private final DtoLLMFilterResponse dtoLLMFilterResponse;

    public LLMFilterResponseEvent(Object source, DtoLLMFilterResponse dtoLLMFilterResponse) {
        super(source);
        this.dtoLLMFilterResponse = dtoLLMFilterResponse;
    }

    public DtoLLMFilterResponse getDtoAIFilterResponse() {
        return dtoLLMFilterResponse;
    }
}
