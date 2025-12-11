package ru.spiiran.sdt_server.domain.event.tle.request;

// Событие для NORAD ID
public class TLENoradIdRequestEvent extends AbstractTLERequestEvent {
    private final Long noradId;

    public TLENoradIdRequestEvent(Object source, Long noradId) {
        super(source);
        this.noradId = noradId;
    }

    public Long getNoradId() {
        return noradId;
    }
}
