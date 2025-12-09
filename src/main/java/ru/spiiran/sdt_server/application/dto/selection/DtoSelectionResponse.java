package ru.spiiran.sdt_server.application.dto.selection;

import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.api.v1.dto.vb.IDtoProgress;

public class DtoSelectionResponse implements IDtoProgress {
    private DtoTimeRegion timeRegion;
    private DtoVBResponse preFiltrationResponse;

    public DtoSelectionResponse(DtoTimeRegion timeRegion, DtoVBResponse preFiltrationResponse) {
        this.timeRegion = timeRegion;
        this.preFiltrationResponse = preFiltrationResponse;
    }

    public DtoTimeRegion getTimeRegion() {
        return timeRegion;
    }

    public void setTimeRegion(DtoTimeRegion timeRegion) {
        this.timeRegion = timeRegion;
    }

    public DtoVBResponse getPreFiltrationResponse() {
        return preFiltrationResponse;
    }

    public void setPreFiltrationResponse(DtoVBResponse preFiltrationResponse) {
        this.preFiltrationResponse = preFiltrationResponse;
    }
}
