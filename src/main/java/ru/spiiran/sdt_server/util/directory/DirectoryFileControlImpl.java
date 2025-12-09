package ru.spiiran.sdt_server.util.directory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.infrastructure.properties.TLEDirectoryProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Component
public class DirectoryFileControlImpl implements DirectoryFileControl {
    private final ClearDirectory clearDirectory;
    private final TLEDirectoryProperties tleDirectoryData;

    public DirectoryFileControlImpl(ClearDirectory clearDirectory, TLEDirectoryProperties tleDirectoryData) {
        this.clearDirectory = clearDirectory;
        this.tleDirectoryData = tleDirectoryData;
    }

    @Override
    public void createDirectoryIfNotExist(Path directory) throws IOException {
        if (Files.notExists(directory)) {
            Files.createDirectories(directory);
        }
    }

    @Override
    public void clearDirectory(Path directory) throws IOException {
        clearDirectory.clearDirectory(directory);
    }

    @Override
    public void moveFilesTo(Path targetDirectory, Path currentDirectory) throws IOException {
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(currentDirectory)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    Path targetFile = targetDirectory.resolve(file.getFileName());
                    Files.move(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
}
