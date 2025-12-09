package ru.spiiran.sdt_server.api.v1.controllers;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBTaskProgress;
import ru.spiiran.sdt_server.application.service.vb.VBServiceImpl;
import ru.spiiran.sdt_server.domain.progress.VBTaskContextHolder;
import ru.spiiran.sdt_server.domain.service.VBTaskProgressService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/vb")
public class RestVirtualBallisticsController {

    private final VBTaskProgressService progressService;
    private final VBServiceImpl vbService;

    public RestVirtualBallisticsController(VBTaskProgressService progressService, VBServiceImpl vbService) {
        this.progressService = progressService;
        this.vbService = vbService;
    }

    @PostMapping
    public String startTask(@RequestBody String prompt, @AuthenticationPrincipal UserDetails userDetails) {
        String taskId = progressService.createTask();
        runAsyncTask(taskId, prompt, userDetails.getUsername());
        return taskId;
    }

    @GetMapping("/progress/{taskId}")
    public DtoVBTaskProgress getProgress(@PathVariable String taskId) {
        return progressService.get(taskId);
    }

    @Async
    public void runAsyncTask(String taskId, String prompt, String username) {
        CompletableFuture.runAsync(() -> {
            try {
                VBTaskContextHolder.setTaskId(taskId);
                progressService.update(taskId, 10, "Анализ запроса...");
                List<DtoVBResponse> responses = vbService.virtualBallisticsRequest(prompt);
                progressService.complete(taskId, responses);
            } catch (Exception e) {
                progressService.error(taskId, e.getMessage());
            } finally {
                VBTaskContextHolder.clear();
            }
        });
    }

}
