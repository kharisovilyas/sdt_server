package ru.spiiran.sdt_server.domain.event.search.response;

import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;

import java.util.List;

public class VBSearchResponseEvent extends AbstractSearchResponseEvent {
    private final List<DtoVBResponse> responses;

    public VBSearchResponseEvent(Object source, List<DtoVBResponse> responses) {
        super(source);
        this.responses = responses;
    }

    public List<DtoVBResponse> getResponses() {
        return responses;
    }


}
