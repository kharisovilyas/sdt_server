package ru.spiiran.sdt_server.infrastructure.file.pro42;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.infrastructure.dto.DtoPro42Coordinate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReadingPro42OutputFileImpl implements ReadingPro42OutputFile {
    @Override
    public List<DtoPro42Coordinate> readingOutputFile(Path outputFile) throws IOException {
        File file = new File(outputFile.toUri());
        List<DtoPro42Coordinate> responsesBallistic = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String jsonString = jsonBuilder.toString();

            // Проверка на пустой файл
            if (jsonString.trim().isEmpty()) {
                return responsesBallistic;
            }

            // Обработка GPS-файлов
            if (jsonString.contains("ModelTime,sec")) {
                return readingOutputGPSFile(outputFile);
            }
        }

        return responsesBallistic;
    }

    private List<DtoPro42Coordinate> readingOutputGPSFile(Path outputFile) throws IOException {
        File file = outputFile.toFile();
        List<DtoPro42Coordinate> coordinates = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty() || line.contains("ModelTime,sec") || line.contains("Longitude") || line.contains("Latitude")) {
                    continue;
                }

                String[] parts = line.trim().split("\\s+");
                if (parts.length == 3) {
                    double timePoint = Double.parseDouble(parts[0]);
                    Long roundTime = Math.round(timePoint);
                    Double latitude = (Double.parseDouble(parts[1]) * 180) / Math.PI;
                    Double longitude = (Double.parseDouble(parts[2]) * 180) / Math.PI;

                    DtoPro42Coordinate coordinate = new DtoPro42Coordinate(roundTime, latitude, longitude);
                    coordinates.add(coordinate);
                }
            }
        }
        return coordinates;
    }
}
