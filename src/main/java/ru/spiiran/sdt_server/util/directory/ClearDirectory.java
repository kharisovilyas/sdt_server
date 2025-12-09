package ru.spiiran.sdt_server.util.directory;

import java.io.IOException;
import java.nio.file.Path;

public interface ClearDirectory {
    void clearDirectory(Path path) throws IOException;
}
