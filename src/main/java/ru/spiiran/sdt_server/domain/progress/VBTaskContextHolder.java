package ru.spiiran.sdt_server.domain.progress;

public class VBTaskContextHolder {
    private static final ThreadLocal<String> taskIdHolder = new ThreadLocal<>();

    public static void setTaskId(String taskId) {
        taskIdHolder.set(taskId);
    }

    public static String getTaskId() {
        return taskIdHolder.get();
    }

    public static void clear() {
        taskIdHolder.remove();
    }
}

