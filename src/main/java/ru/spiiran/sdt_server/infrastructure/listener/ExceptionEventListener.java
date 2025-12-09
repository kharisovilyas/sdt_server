package ru.spiiran.sdt_server.infrastructure.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.domain.event.exception.ExceptionEvent;

@Component
public class ExceptionEventListener {

    @EventListener
    public void handleExceptionEvent(ExceptionEvent event) {
        // Пока пусто, сюда можно добавить логи, алерты, метрики и т.п.
        System.out.println("Получено исключение: " + event.getException() + " с сообщением: " + event.getMessage());
    }
}
