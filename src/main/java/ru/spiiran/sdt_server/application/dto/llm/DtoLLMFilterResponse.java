package ru.spiiran.sdt_server.application.dto.llm;

import ru.spiiran.sdt_server.api.v1.dto.vb.IDtoProgress;

public class DtoLLMFilterResponse implements IDtoProgress {
    private DtoFilters filters;
    private Boolean valid;
    private Long timestamp;
    private String raw;

    public DtoLLMFilterResponse(DtoFilters filters, Boolean valid, Long timestamp, String raw) {
        this.filters = filters;
        this.valid = valid;
        this.timestamp = timestamp;
        this.raw = raw;
    }

    public DtoFilters getFilters() {
        return filters;
    }

    public void setFilters(DtoFilters filters) {
        this.filters = filters;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}

