package ru.spiiran.sdt_server.application.dto.llm;

public class DtoLLMRequest {
    private String text;

    public DtoLLMRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
