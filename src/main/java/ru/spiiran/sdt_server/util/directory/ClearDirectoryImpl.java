package ru.spiiran.sdt_server.util.directory;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class ClearDirectoryImpl implements ClearDirectory {
    @Override
    public void clearDirectory(Path path) throws IOException {

    }
}
