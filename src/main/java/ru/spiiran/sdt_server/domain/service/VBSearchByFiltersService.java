package ru.spiiran.sdt_server.domain.service;

import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.application.dto.llm.DtoFilters;

import java.util.List;

public interface VBSearchByFiltersService {
    List<DtoVBResponse> searchByFilters(DtoFilters filters);
}
