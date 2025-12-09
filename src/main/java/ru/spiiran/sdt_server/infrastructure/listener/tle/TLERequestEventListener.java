package ru.spiiran.sdt_server.infrastructure.listener.tle;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.domain.event.tle.request.TLENoradIdRequestEvent;

@Component
public class TLERequestEventListener {

    @EventListener
    public void onNoradIdRequest(TLENoradIdRequestEvent event) {
        Long noradId = event.getNoradId();
        System.out.println("Обработан запрос на получение TLE для NORAD ID: " + noradId + " at " + java.time.LocalDateTime.now());
    }
}
