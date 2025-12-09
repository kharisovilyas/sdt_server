package ru.spiiran.sdt_server.application.service.vb;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.application.dto.llm.DtoLLMFilterResponse;
import ru.spiiran.sdt_server.application.dto.selection.DtoSelectionResponse;
import ru.spiiran.sdt_server.domain.event.llm.request.LLMRequestEvent;
import ru.spiiran.sdt_server.domain.event.llm.response.VBLLMResponseEvent;
import ru.spiiran.sdt_server.domain.event.optimization.response.VBOptimizationResponseEvent;
import ru.spiiran.sdt_server.domain.event.search.response.VBSearchResponseEvent;
import ru.spiiran.sdt_server.domain.event.selection.response.VBSelectionResponseEvent;
import ru.spiiran.sdt_server.domain.progress.VBTaskContextHolder;
import ru.spiiran.sdt_server.domain.service.*;
import ru.spiiran.sdt_server.infrastructure.client.llm.LLMClient;
import ru.spiiran.sdt_server.infrastructure.exception.BadRequestException;
import ru.spiiran.sdt_server.infrastructure.exception.llm.*;
import ru.spiiran.sdt_server.infrastructure.exception.mapper.ExtremePointsMappingException;

import java.util.List;

@Service
public class VBServiceImpl implements VBService {
    private final LLMClient LLMClient;
    private final VBSearchByFiltersService searchByFiltersService;
    private final VBSelectionRegionService selectionRegionService;
    private final VBOptimizationService optimizationService;
    private final ApplicationEventPublisher eventPublisher;
    private final VBTaskProgressService progressService;

    public VBServiceImpl(
            LLMClient LLMClient, VBSearchByFiltersService searchByFiltersService,
            VBSelectionRegionService selectionRegionService,
            VBOptimizationService optimizationService,
            ApplicationEventPublisher eventPublisher, VBTaskProgressService progressService
    ) {
        this.LLMClient = LLMClient;
        this.searchByFiltersService = searchByFiltersService;
        this.selectionRegionService = selectionRegionService;
        this.optimizationService = optimizationService;
        this.eventPublisher = eventPublisher;
        this.progressService = progressService;
    }

    @Override
    public List<DtoVBResponse> virtualBallisticsRequest(String prompt)
            throws AuthenticationLLMException, BadRequestException, ConnectionLLMException,
            ServerErrorLLMException, TimeoutLLMException, BadResponseLLMException, ExtremePointsMappingException
    {
        String currentTaskId = VBTaskContextHolder.getTaskId();
        validateRequest(prompt);
        /// --------------------- Работа с сервисом LLM ---------------------
        DtoLLMFilterResponse llmResponse = llmRequest(prompt, currentTaskId);

        /// ------------- Работа с атрибутным запросом к СУБД --------------
        List<DtoVBResponse> searchByFilterResponses = searchByFilters(llmResponse, currentTaskId);

        /// ------------------- Работа с сервисом Pro42 --------------------
        List<DtoSelectionResponse> selectionResponses = selectionRegionRequest(llmResponse, searchByFilterResponses, currentTaskId);

        /// ---------- Работа с многокритериальной оптимизацией -----------
        return multicriteriaOptimization(llmResponse, selectionResponses, currentTaskId);
    }

    private List<DtoVBResponse> multicriteriaOptimization(DtoLLMFilterResponse llmResponse, List<DtoSelectionResponse> selectionResponses, String currentTaskId) {
        // 1 Событие о запросе к оптимизации
        // 2 Обновление статуса задачи
        progressService.update(currentTaskId, 80, "Многокритериальная оптимизация...");
        // 3 Вызов оптимизации
        Integer numberOfSatellite = numberOfSatelliteFromResponse(llmResponse);
        List<DtoVBResponse> vbResponses = optimizationService.multicriteriaOptimization(selectionResponses, numberOfSatellite);
        // 4 Событие об ответе оптимизации
        eventPublisher.publishEvent(new VBOptimizationResponseEvent(this, vbResponses));
        // 5 Обновление статуса задачи на выполнено
        progressService.update(currentTaskId, 100, "Готово!");
        return vbResponses;
    }

    private List<DtoSelectionResponse> selectionRegionRequest(DtoLLMFilterResponse llmResponse, List<DtoVBResponse> searchByFilterResponses, String currentTaskId) {
        // 1 Событие о запросе к Pro42
        // 2 Обновление статуса задачи
        progressService.update(currentTaskId, 60, "Имитационное моделирование...");
        // 3 Вызов Pro42
        List<DtoSelectionResponse> selectionResponses = selectionRegionService.selectionByTargetRegion(llmResponse, searchByFilterResponses);
        // 4 Событие об ответе Pro42
        eventPublisher.publishEvent(new VBSelectionResponseEvent(this, selectionResponses));
        // 5 Добавление информации о результатах этапа сквозного процесса
        progressService.addMapProgressInform(currentTaskId, DtoSelectionResponse.class.getName(), selectionResponses);
        return selectionResponses;
    }

    private List<DtoVBResponse> searchByFilters(DtoLLMFilterResponse llmResponse, String currentTaskId) {
        // 1 Событие о запросе к атрибутному фильтру
        // 2 Обновление статуса задачи
        progressService.update(currentTaskId, 40, "Поиск подходящих аппаратов...");
        // 3 Вызов атрибутного фильтра к БД
        List<DtoVBResponse> searchByFilterResponses = searchByFiltersService.searchByFilters(llmResponse.getFilters());
        // 4 Событие об ответе атрибутной выборки
        eventPublisher.publishEvent(new VBSearchResponseEvent(this, searchByFilterResponses));
        // 5 Добавление информации о результатах этапа сквозного процесса
        progressService.addMapProgressInform(currentTaskId, DtoVBResponse.class.getName(), searchByFilterResponses);
        return searchByFilterResponses;
    }

    private DtoLLMFilterResponse llmRequest(String prompt, String currentTaskId) throws ConnectionLLMException {
        // 1 Событие о запросе к LLM
        eventPublisher.publishEvent(new LLMRequestEvent(this, prompt));
        // 2 Обновления статуса задачи
        progressService.update(currentTaskId, 20, "Анализ запроса LLM...");
        // 3 Вызов клиента LLM
        DtoLLMFilterResponse filterResponse = LLMClient.llmRequest(prompt);
        // 4 Событие об ответе клиента LLM
        eventPublisher.publishEvent(new VBLLMResponseEvent(this, filterResponse));
        // 5 Добавление информации о результатах этапа сквозного процесса
        progressService.addMapProgressInform(currentTaskId, DtoLLMFilterResponse.class.getName(), List.of(filterResponse));
        return filterResponse;
    }

    private void validateRequest(String prompt) throws IllegalArgumentException, BadRequestException{
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("Prompt не может быть пустым");
        }
        if (prompt.length() > 2000) {
            throw new BadRequestLLMException("Слишком длинный prompt для AI-сервиса");
        }
    }

    private Integer numberOfSatelliteFromResponse(DtoLLMFilterResponse filterResponse) {
        String numberStr = filterResponse.getFilters().getNumber();
        return (numberStr != null && !numberStr.trim().isEmpty())
                ? Integer.valueOf(numberStr.trim())
                : null;
    }
}
