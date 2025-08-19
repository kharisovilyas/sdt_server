package ru.spiiran.sdt_server.utils.exception;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.spiiran.sdt_server.obj.event.exception.ExceptionEvent;
import ru.spiiran.sdt_server.utils.exception.sup.AuthenticationException;
import ru.spiiran.sdt_server.utils.exception.sup.NotFoundException;
import ru.spiiran.sdt_server.utils.exception.sup.RequestException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiValidationExceptionHandler {

    private final ApplicationEventPublisher eventPublisher;

    public ApiValidationExceptionHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @ExceptionHandler(value = {
            RequestException.class,
            NotFoundException.class,
            AuthenticationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected Map<String, Object> handleBadRequest(RuntimeException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", ex.getMessage());
        publishExceptionEvent(ex, errorResponse);
        return errorResponse;
    }

    private void publishExceptionEvent(RuntimeException ex, Map<String, Object> errorResponse) {
        ExceptionEvent event = new ExceptionEvent(
                ex.getMessage(),
                ex.getClass().getName(),
                errorResponse,
                determineHttpStatus(ex),
                determinePriority(ex)
        );
        eventPublisher.publishEvent(event);
    }

    private String determineHttpStatus(RuntimeException ex) {
        if (ex instanceof AuthenticationException) {
            return HttpStatus.UNAUTHORIZED.toString(); // 401
        } else if (ex instanceof NotFoundException) {
            return HttpStatus.NOT_FOUND.toString(); // 404
        } else if (ex instanceof RequestException) {
            return HttpStatus.BAD_REQUEST.toString(); // 400
        }
        return HttpStatus.INTERNAL_SERVER_ERROR.toString(); //500
    }

    private String determinePriority(RuntimeException ex) {
        if (ex instanceof AuthenticationException || ex instanceof RequestException) {
            return "HIGH";
        } else if (ex instanceof NotFoundException) {
            return "MEDIUM";
        }
        return "LOW";
    }
}