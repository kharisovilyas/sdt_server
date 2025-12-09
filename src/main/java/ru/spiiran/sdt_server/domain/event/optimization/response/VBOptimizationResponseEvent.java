package ru.spiiran.sdt_server.domain.event.optimization.response;

import org.springframework.context.ApplicationEvent;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;

import java.util.List;

public class VBOptimizationResponseEvent extends ApplicationEvent {
    private final List<DtoVBResponse> vbResponse;
    public VBOptimizationResponseEvent(Object object, List<DtoVBResponse> vbResponses) {
        super(object);
        this.vbResponse = vbResponses;
    }
    public List<DtoVBResponse> getVbResponse() {
        return vbResponse;
    }
}
