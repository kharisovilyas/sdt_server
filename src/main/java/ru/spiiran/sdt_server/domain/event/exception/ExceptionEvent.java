package ru.spiiran.sdt_server.domain.event.exception;

import java.util.Map;

public class ExceptionEvent {
    private String message;
    private String exception;
    private Map<String, Object> errorResponse;
    private String errorCode;
    private String errorPriority;

    public ExceptionEvent(String message, String exception, Map<String, Object> errorResponse, String errorCode, String errorPriority) {
        this.message = message;
        this.exception = exception;
        this.errorResponse = errorResponse;
        this.errorCode = errorCode;
        this.errorPriority = errorPriority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Map<String, Object> getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(Map<String, Object> errorResponse) {
        this.errorResponse = errorResponse;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorPriority() {
        return errorPriority;
    }

    public void setErrorPriority(String errorPriority) {
        this.errorPriority = errorPriority;
    }
}
