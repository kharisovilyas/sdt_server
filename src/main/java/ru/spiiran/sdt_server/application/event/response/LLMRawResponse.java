package ru.spiiran.sdt_server.application.event.response;

public class LLMRawResponse extends AbstractLLMRawResponse {
    private String raw;
    public LLMRawResponse(Object source, String raw) {
        super(source);
        this.raw = raw;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}
