package ru.spiiran.sdt_server.obj.event.tle.request;

// Событие для NORAD ID
public class tleNoradIdRequestEvent extends AbstractTLERequestEvent {
    private final Long noradId;

    public tleNoradIdRequestEvent(Long noradId) {
        this.noradId = noradId;
    }

    public Long getNoradId() {
        return noradId;
    }
}
