package ru.spiiran.sdt_server.domain.event.llm.response;

import ru.spiiran.sdt_server.application.dto.llm.DtoLLMFilterResponse;

public class VBLLMResponseEvent extends AbstractLLMResponseEvent{
    private DtoLLMFilterResponse filterResponse;
    public DtoLLMFilterResponse getFilterResponse() {
        return filterResponse;
    }
    public VBLLMResponseEvent(Object source, DtoLLMFilterResponse filterResponse) {
        super(source);
        this.filterResponse = filterResponse;
    }
}
