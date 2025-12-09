package ru.spiiran.sdt_server.domain.service;

import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.application.dto.llm.DtoLLMFilterResponse;
import ru.spiiran.sdt_server.application.dto.selection.DtoSelectionResponse;

import java.util.List;

public interface VBSelectionRegionService {
    List<DtoSelectionResponse> selectionByTargetRegion(DtoLLMFilterResponse filterResponse, List<DtoVBResponse> searchByFilterResponses);
}
