package ru.spiiran.sdt_server.infrastructure.file.pro42;

import ru.spiiran.sdt_server.infrastructure.dto.DtoPro42Coordinate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ReadingPro42OutputFile {
    List<DtoPro42Coordinate> readingOutputFile(Path outputFile) throws IOException;
}
