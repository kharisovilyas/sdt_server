package ru.spiiran.sdt_server.util.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class TimeConverter {
    private static final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ISO_INSTANT,
            DateTimeFormatter.RFC_1123_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd")
    );

    private static final Pattern UNIX_TIMESTAMP_PATTERN = Pattern.compile("^\\d+$");

    public static Long timeUdtToUnix(String date) {
        if (date == null || date.trim().isEmpty()) {
            return null;
        }

        String dateStr = date.trim();

        // Проверяем, является ли строка Unix timestamp
        if (UNIX_TIMESTAMP_PATTERN.matcher(dateStr).matches()) {
            try {
                long timestamp = Long.parseLong(dateStr);
                // Если timestamp в миллисекундах, преобразуем в секунды
                if (dateStr.length() > 10) {
                    return timestamp / 1000;
                }
                return timestamp;
            } catch (NumberFormatException e) {
                // Продолжим попытки парсинга другими способами
            }
        }

        // Пытаемся распарсить различными форматами дат
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                // Пытаемся парсить как дату-время с часовым поясом
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr, formatter);
                return zonedDateTime.toEpochSecond();
            } catch (DateTimeParseException e) {
                // Игнорируем и пробуем следующий формат
            }

            try {
                // Пытаемся парсить как локальную дату-время (без часового пояса)
                LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
                return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
            } catch (DateTimeParseException e) {
                // Игнорируем и пробуем следующий формат
            }

            try {
                // Пытаемся парсить как дату (без времени)
                LocalDate localDate = LocalDate.parse(dateStr, formatter);
                return localDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
            } catch (DateTimeParseException e) {
                // Игнорируем и пробуем следующий формат
            }
        }

        // Если ни один формат не подошел
        return null;
    }
}