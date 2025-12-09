package ru.spiiran.sdt_server.infrastructure.file.pro42;

import java.io.IOException;
import java.nio.file.Path;

public interface GenericPro42Files {
    void init(Path armDirectory);
    void createSimFile(Long satelliteId) throws IOException;
    void createCsgFlagFile() throws IOException;
    void createOrbFiles(String tle, Long satelliteId) throws IOException;
    void createScFiles(Long satelliteId) throws IOException;
    void createTLEFile(String tle) throws IOException;
}
