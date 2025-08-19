package ru.spiiran.sdt_server.bus.listenner.tle;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.obj.event.tle.request.*;

@Component
public class TLERequestEventListener {

    @EventListener
    public void onNoradIdRequest(tleNoradIdRequestEvent event) {
        Long noradId = event.getNoradId();
        System.out.println("Обработан запрос на получение TLE для NORAD ID: " + noradId + " at " + java.time.LocalDateTime.now());
    }
}
