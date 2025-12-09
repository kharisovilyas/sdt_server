package ru.spiiran.sdt_server.util.directory;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface DirectoryFileControl {
    void createDirectoryIfNotExist(Path pro42Directory) throws IOException;
    void clearDirectory(Path directory) throws IOException;
    void moveFilesTo(Path targetDirectory, Path currentDirectory) throws IOException;
}
