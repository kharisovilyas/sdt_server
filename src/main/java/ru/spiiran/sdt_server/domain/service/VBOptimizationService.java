package ru.spiiran.sdt_server.domain.service;

import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.application.dto.selection.DtoSelectionResponse;

import java.util.List;

public interface VBOptimizationService {
    List<DtoVBResponse> multicriteriaOptimization(List<DtoSelectionResponse> selectionResponses, Integer numberOfSatellites);
}
