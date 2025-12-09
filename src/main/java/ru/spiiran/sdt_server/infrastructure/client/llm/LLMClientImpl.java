package ru.spiiran.sdt_server.infrastructure.client.llm;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.application.dto.llm.DtoLLMFilterResponse;
import ru.spiiran.sdt_server.application.dto.llm.DtoLLMRequest;
import ru.spiiran.sdt_server.application.event.response.LLMFilterResponseEvent;
import ru.spiiran.sdt_server.application.event.response.LLMRawResponse;
import ru.spiiran.sdt_server.domain.service.VBSearchByFiltersService;
import ru.spiiran.sdt_server.infrastructure.exception.llm.BadResponseLLMException;
import ru.spiiran.sdt_server.infrastructure.exception.llm.ConnectionLLMException;
import ru.spiiran.sdt_server.infrastructure.exception.llm.ServerErrorLLMException;
import ru.spiiran.sdt_server.infrastructure.exception.llm.TimeoutLLMException;
import ru.spiiran.sdt_server.infrastructure.properties.LLMProperties;

import java.net.SocketTimeoutException;
import java.util.List;

@Component
public class LLMClientImpl implements LLMClient {

    private final RestTemplate restTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final LLMProperties LLMProperties;

    public LLMClientImpl(RestTemplate restTemplate, ApplicationEventPublisher eventPublisher, LLMProperties LLMProperties) {
        this.restTemplate = restTemplate;
        this.eventPublisher = eventPublisher;
        this.LLMProperties = LLMProperties;
    }

    @Override
    public DtoLLMFilterResponse llmRequest(String prompt)
            throws ConnectionLLMException, ServerErrorLLMException,
            TimeoutLLMException, BadResponseLLMException
    {
        DtoLLMFilterResponse filterResponse = llmFilterRequest(prompt);
        // публикуем событие сразу для этого фильтра
        eventPublisher.publishEvent(new LLMFilterResponseEvent(this, filterResponse));
        if (!filterResponse.getValid()) {
            eventPublisher.publishEvent(new LLMRawResponse(this, filterResponse.getRaw()));
            throw new BadResponseLLMException("Модель отработала некорректно, выходной json поврежден");
        }
        return filterResponse;
    }

    private DtoLLMFilterResponse llmFilterRequest(String prompt)
            throws ConnectionLLMException, ServerErrorLLMException, TimeoutLLMException {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            DtoLLMRequest requestDto = new DtoLLMRequest(prompt);
            HttpEntity<DtoLLMRequest> entity = new HttpEntity<>(requestDto, headers);
            String aiUrl = "http://" + LLMProperties.getHost() + ":" + LLMProperties.getPort() + "/parse";

            // ожидаем один объект, а не массив
            ResponseEntity<DtoLLMFilterResponse> response =
                    restTemplate.postForEntity(aiUrl, entity, DtoLLMFilterResponse.class);

            if (response.getStatusCode().is5xxServerError()) {
                throw new ServerErrorLLMException("Ошибка на стороне AI сервера");
            }

            if (response.getBody() == null) {
                throw new ConnectionLLMException("AI сервис вернул пустой фильтр");
            }

            return response.getBody();

        } catch (HttpServerErrorException ex) {
            throw new ServerErrorLLMException("Ошибка сервера AI: " + ex.getMessage());
        } catch (ResourceAccessException ex) {
            if (ex.getCause() instanceof SocketTimeoutException) {
                throw new TimeoutLLMException("AI сервис не ответил вовремя");
            }
            throw new ConnectionLLMException("Нет соединения с AI сервисом");
        } catch (RestClientException ex) {
            throw new ConnectionLLMException("Ошибка при вызове AI сервиса");
        }
    }
}
