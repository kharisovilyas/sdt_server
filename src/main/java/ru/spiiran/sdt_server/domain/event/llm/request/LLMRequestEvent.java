package ru.spiiran.sdt_server.domain.event.llm.request;

public class LLMRequestEvent extends AbstractLLMRequestEvent {
    private String prompt;

    public LLMRequestEvent(Object source, String prompt) {
        super(source);
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
