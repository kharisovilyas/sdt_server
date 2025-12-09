package ru.spiiran.sdt_server.api.v1.dto.vb;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DtoVBTaskProgress {
    private String taskId;
    private int percent;
    private String stage;
    private boolean completed;
    private List<DtoVBResponse> result;
    private Map<String, List<? extends IDtoProgress>> mapProgress = new LinkedHashMap<>();
    private String status;
    private String error;

    public DtoVBTaskProgress() {
    }

    public Map<String, List<? extends IDtoProgress>> getMapProgress() {
        return mapProgress;
    }

    public void setMapProgress(Map<String, List<? extends IDtoProgress>> mapProgress) {
        this.mapProgress = mapProgress;
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<DtoVBResponse> getResult() {
        return result;
    }

    public void setResult(List<DtoVBResponse> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
