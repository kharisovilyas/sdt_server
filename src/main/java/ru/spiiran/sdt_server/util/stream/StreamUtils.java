package ru.spiiran.sdt_server.util.stream;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.function.ThrowingFunction;

import java.util.function.Function;

public class StreamUtils {
    @Transactional
    public static <T, R> Function<T, R> wrapException(ThrowingFunction<T, R> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                throw new RuntimeException("Ошибка во время обработки потока", e);
            }
        };
    }
}
