package ru.spiiran.sdt_server.infrastructure.file.pro42;

import java.io.IOException;

public interface FilePro42 {
    void genericPro42Files(Long satelliteId, String tle) throws IOException;
}
