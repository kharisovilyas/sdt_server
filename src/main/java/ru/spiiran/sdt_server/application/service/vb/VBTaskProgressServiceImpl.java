package ru.spiiran.sdt_server.application.service.vb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBTaskProgress;
import ru.spiiran.sdt_server.api.v1.dto.vb.IDtoProgress;
import ru.spiiran.sdt_server.domain.service.VBTaskProgressService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервис для управления прогрессом выполнения задач VB.
 * Потокобезопасный, хранит все задачи в памяти.
 */
@Service
public class VBTaskProgressServiceImpl implements VBTaskProgressService {

    private static final Logger logger = LoggerFactory.getLogger(VBTaskProgressServiceImpl.class);

    /** Хранилище всех активных и завершённых задач */
    private final Map<String, DtoVBTaskProgress> tasks = new ConcurrentHashMap<>();

    /** Создание новой задачи */
    @Override
    public String createTask() {
        String taskId = UUID.randomUUID().toString();
        DtoVBTaskProgress progress = new DtoVBTaskProgress();
        progress.setTaskId(taskId);
        progress.setPercent(0);
        progress.setStage("Ожидание...");
        progress.setCompleted(false);

        tasks.put(taskId, progress);
        logger.info("Создана новая задача VB: {}", taskId);

        return taskId;
    }

    /** Получение состояния задачи по ID */
    @Override
    public DtoVBTaskProgress get(String taskId) {
        return tasks.get(taskId);
    }

    /** Обновление процента выполнения и стадии */
    @Override
    public void update(String taskId, int percent, String stage) {
        tasks.computeIfPresent(taskId, (id, progress) -> {
            progress.setPercent(percent);
            progress.setStage(stage);
            progress.setStatus("PROGRESS");
            logger.debug("Задача {} обновлена: {}% ({})", id, percent, stage);
            return progress;
        });
    }

    /** Завершение задачи с результатом */
    @Override
    public void complete(String taskId, List<DtoVBResponse> result) {
        tasks.computeIfPresent(taskId, (id, progress) -> {
            progress.setPercent(100);
            progress.setStage("Готово!");
            progress.setCompleted(true);
            progress.setResult(result);
            progress.setStatus("COMPLETED");
            logger.info("Задача {} завершена успешно", id);
            return progress;
        });
    }

    /** Завершение задачи с ошибкой */
    @Override
    public void error(String taskId, String message) {
        tasks.computeIfPresent(taskId, (id, progress) -> {
            progress.setStage("Ошибка");
            progress.setCompleted(true);
            progress.setError(message);
            progress.setStatus("ERROR");
            logger.error("Ошибка при выполнении задачи {}: {}", id, message);
            return progress;
        });
    }

    @Override
    public void addMapProgressInform(String taskId, String className, List<? extends IDtoProgress> selectionResponses) {
        tasks.computeIfPresent(taskId, (id, progress) -> {
            Map<String, List<? extends IDtoProgress>> map = progress.getMapProgress();
            if (map == null) {
                map = new LinkedHashMap<>();
                progress.setMapProgress(map);
            }
            map.put(className, selectionResponses);
            logger.debug("Для задачи {} добавлена информация о прогрессе: {}", id, className);
            return progress;
        });
    }


    /** (необязательно) Очистка старых задач по таймеру или вручную */
    public void clearCompleted() {
        tasks.entrySet().removeIf(entry -> entry.getValue().isCompleted());
        logger.info("Завершённые задачи очищены");
    }
}
