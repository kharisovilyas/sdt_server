package ru.spiiran.sdt_server.infrastructure.file.pro42;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface Pro42DirectoryControl {
    void init(String username);
    void createDirectoriesIfNotExist() throws IOException;
    void clearPro42Directory() throws IOException;
    void moveFilesToBallistic() throws IOException;
    String getDirectoryForCommand();
    Path getPro42Directory();
    String getRunDirectory();
    String getOutputFileName();
    Path getArmDirectory();
}
