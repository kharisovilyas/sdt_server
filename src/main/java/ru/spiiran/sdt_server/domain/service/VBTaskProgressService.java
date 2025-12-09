package ru.spiiran.sdt_server.domain.service;

import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBTaskProgress;
import ru.spiiran.sdt_server.api.v1.dto.vb.IDtoProgress;

import java.util.List;

public interface VBTaskProgressService {
    String createTask();
    DtoVBTaskProgress get(String taskId);
    void update(String taskId, int percent, String stage);
    void complete(String taskId, List<DtoVBResponse> result);
    void error(String taskId, String message);
    void addMapProgressInform(String taskId, String className, List<? extends IDtoProgress> selectionResponses);
}
