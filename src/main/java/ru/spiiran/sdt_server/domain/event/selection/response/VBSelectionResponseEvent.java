package ru.spiiran.sdt_server.domain.event.selection.response;

import org.springframework.context.ApplicationEvent;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.application.dto.selection.DtoSelectionResponse;
import ru.spiiran.sdt_server.application.service.vb.VBServiceImpl;

import java.util.List;

public class VBSelectionResponseEvent extends AbstractSelectionResponseEvent {
    private List<DtoSelectionResponse> selectionResponses;
    public VBSelectionResponseEvent(Object object, List<DtoSelectionResponse> selectionResponses) {
        super(object);
        this.selectionResponses = selectionResponses;
    }
    public List<DtoSelectionResponse> getSelectionResponses() {
        return selectionResponses;
    }
}
