package ru.spiiran.sdt_server.infrastructure.file.pro42;

import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.infrastructure.properties.Pro42DirectoryProperties;
import ru.spiiran.sdt_server.infrastructure.properties.Pro42Properties;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenericPro42FilesImpl implements GenericPro42Files {
    private Path armDirectory;
    private final SearchPro42TemplatesFile searchPro42TemplatesFile;
    private final Pro42Properties pro42Properties;
    private final Pro42DirectoryProperties pro42DirectoryProperties;

    public GenericPro42FilesImpl(SearchPro42TemplatesFile searchPro42TemplatesFile, Pro42Properties pro42Properties, Pro42DirectoryProperties pro42DirectoryProperties) {
        this.searchPro42TemplatesFile = searchPro42TemplatesFile;
        this.pro42Properties = pro42Properties;
        this.pro42DirectoryProperties = pro42DirectoryProperties;
    }

    @Override
    public void init(Path armDirectory) {
        this.armDirectory = armDirectory;
    }

    @Override
    public void createSimFile(Long satelliteId) throws IOException {
        // Ключевые фразы для поиска
        List<String> keywords = getKeywordForSim();

        //10000.0   0.1                   !  Sim Duration, Step Size [sec]
        List<String> replacements = getStrings(satelliteId);

        // Чтение файла
        File file = searchPro42TemplatesFile.searchFileSim();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        // Чтение файла построчно и замена строк с ключевыми фразами
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
            if (line.contains(keywords.get(0))) {
                // Пропускаем 2 строки после найденной
                stringBuilder.append(reader.readLine()).append("\n");
                stringBuilder.append(replacements.get(0)).append("\n");
                reader.readLine(); // Пропускаем следующую строку

            } else if (line.contains(keywords.get(1))) {
                // Пропускаем одну строку после найденной
                stringBuilder.append(replacements.get(1)).append("\n");
                reader.readLine(); // Пропускаем следующую строку
                reader.readLine(); // Пропускаем следующую строку
            } else if (line.contains(keywords.get(2))) {
                // Пропускаем одну строку после найденной
                stringBuilder.append(replacements.get(2)).append("\n");
                reader.readLine(); // Пропускаем следующую строку
                reader.readLine(); // Пропускаем следующую строку
            } else if (line.contains(keywords.get(3))) {
                // Пропускаем одну строку после найденной
                stringBuilder.append(replacements.get(3)).append("\n");
                reader.readLine(); // Пропускаем следующую строку
                reader.readLine(); // Пропускаем следующую строку
            }
        }
        reader.close();

        // Создаем путь к файлу в текущей директории
        String filePath = armDirectory + pro42DirectoryProperties.TEMPLATE_FILE_SIM();

        Path uploadFilePath = Paths.get(filePath);
        Files.writeString(uploadFilePath, stringBuilder.toString());
    }

    private List<String> getStrings(Long satelliteId) {
        String simulationControlLine = getSimulationControlLine();

        //1                               !  Number of Reference Orbits
        //TRUE   Orb_LEO.txt              !  Input file name for Orb 0
        String referenceOrbits = getReferenceOrbits(satelliteId);

        //1                               !  Number of Spacecraft
        //TRUE  0 SC_Empty.txt           !  Existence, RefOrb, Input file for SC 0
        String spacecraft = getSpacecraft(satelliteId);

        // Создаем строку environment
        //10 18 2025                      !  Date (UTC) (Month, Day, Year)
        //08 30 00.00                     !  Time (UTC) (Hr,Min,Sec)
        String environment = getEnvironment();

        // Содержимое для замены
        List<String> replacements = new ArrayList<>();
        replacements.add(simulationControlLine);
        replacements.add(referenceOrbits);
        replacements.add(spacecraft);
        replacements.add(environment);
        replacements.add(replacementsLeapSecond());
        return replacements;
    }

    private String replacementsLeapSecond() {
        return "-32.0                            !  Leap Seconds (sec)";
    }

    //10 18 2025                      !  Date (UTC) (Month, Day, Year)
    //08 30 00.00                     !  Time (UTC) (Hr,Min,Sec)
    private String getEnvironment() {
        // Получаем время в формате Unix
        long unixTime = System.currentTimeMillis() / pro42Properties.CONVERT_TO_SEC();

        // Конвертируем время Unix в объект LocalDateTime
        Instant instant = Instant.ofEpochSecond(unixTime);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));

        // Форматируем дату и время в указанный формат
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM dd yyyy HH mm ss.SS");
        String formattedDateTime = dateTime.format(formatter);

        return formattedDateTime.substring(0, 10) + "                      !  Date (UTC) (Month, Day, Year)\n" +
                formattedDateTime.substring(11) + "                     !  Time (UTC) (Hr,Min,Sec)";
    }

    //1                               !  Number of Spacecraft
    //TRUE  0 SC_Empty.txt           !  Existence, RefOrb, Input file for SC 0
    private String getSpacecraft(Long satelliteId) {
        return String.format("""
            1                               !  Number of Spacecraft
            TRUE  0 SC_%d.txt               !  Existence, RefOrb, Input file for SC %d""", satelliteId, satelliteId);
    }

    //1                               !  Number of Reference Orbits
    //TRUE   Orb_23.txt              !  Input file name for Orb 23
    private String getReferenceOrbits(Long satelliteId) {
        return String.format("""
            1                               !  Number of Reference Orbits
            TRUE   Orb_%d.txt               !  Input file name for Orb %d""", satelliteId, satelliteId);
    }

    //10000.0   0.1                   !  Sim Duration, Step Size [sec]
    // ИЛИ
    //0.0   0.1                   !  Sim Duration, Step Size [sec]
    private String getSimulationControlLine() {
        Long modellingDuration = pro42Properties.DURATION();
        double modellingStep = pro42Properties.STEP();
        return modellingDuration + "   " + modellingStep + "                   !  Sim Duration, Step Size [sec]";
    }

    @Override
    public void createCsgFlagFile() throws IOException {
        // Чтение файла
        File file = searchPro42TemplatesFile.searchFileFlag();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        String keyword = getKeywordForFlags();
        //TRUE					!  Flag Flight Data calculate
        String replacement = "TRUE\t\t\t\t\t!  " + keyword;

        // Чтение файла построчно и замена строк с ключевыми фразами
        while ((line = reader.readLine()) != null) {
            if (!keyword.isEmpty() && line.contains(keyword)) {
                stringBuilder.append(replacement).append("\n");
            } else {
                stringBuilder.append(line).append("\n");
            }
        }
        reader.close();

        // Создаем путь к файлу в текущей директории
        String filePath = armDirectory + pro42DirectoryProperties.TEMPLATE_FILE_FLAG();

        // Запись измененного содержимого обратно в файл
        File uploadFile = new File(filePath);
        FileWriter writer = new FileWriter(uploadFile);
        writer.write(stringBuilder.toString());
        writer.close();
    }

    @Override
    public void createOrbFiles(String tle, Long satelliteId) throws IOException {
        //TLE                           !  Use Keplerian elements (KEP) or (RV) or FILE
        //TLE  "EXAMPLE 1"              !  TLE or TRV format, Label to find in file
        //"TlE.txt"                     !  File name
        String replacementUseFile = "FILE                           !  Use Keplerian elements (KEP) or (RV) or FILE";
        String replacementFileName = "\"TLE.txt\"                     !  File name";
        String scLabel = tle.split("\\R", 2)[0];
        String replacementFormatTLE = "TLE  \"" + scLabel + "\"              !  TLE or TRV format, Label to find in file";
        File file = searchPro42TemplatesFile.searchFileOrb();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        int currentLine = 0;

        // Чтение файла построчно и замена строк с ключевыми фразами
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
            currentLine++;
            if (currentLine == 12) {
                reader.readLine(); // Пропускаем следующую строку
                stringBuilder.append(replacementUseFile).append("\n");
            }
            if (currentLine == 21) {
                reader.readLine(); // Пропускаем следующую строку
                reader.readLine(); // Пропускаем следующую строку
                stringBuilder.append(replacementFormatTLE)
                        .append("\n")
                        .append(replacementFileName)
                        .append("\n");

            }

        }
        reader.close();
        // Создаем путь к файлу в текущей директории
        String filePath = this.armDirectory + pro42DirectoryProperties.TEMPLATE_FILE_ORB() + satelliteId + ".txt";

        // Запись измененного содержимого обратно в файл
        File uploadFile = new File(filePath);
        FileWriter writer = new FileWriter(uploadFile);
        writer.write(stringBuilder.toString());
        writer.close();
    }

    @Override
    public void createScFiles(Long satelliteId) throws IOException {
        //"S/C"                         !  Label
        String label = "\"" + satelliteId + "\"" + "                         !  Label";

        String keyWordGPS = "* GPS *";
        String replacementForGPS = """
                1                             ! Number of GPS Receivers
                ============================= GPSR 0 ====================================
                5.0                           ! Sample Time,sec
                0.0                           ! Position Noise, m RMS
                0.0                           ! Velocity Noise, m/sec RMS
                0.0                           ! Time Noise, sec RMS
                0                             ! Flex Node Index
                """;

        // Чтение файла
        File file = searchPro42TemplatesFile.searchFileSc();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        int currentLine = 0;

        // Чтение файла построчно и замена строк с ключевыми фразами
        while ((line = reader.readLine()) != null) {
            currentLine++;
            stringBuilder.append(line).append("\n");
            if (currentLine == 2) {
                stringBuilder.append(label).append("\n");
                reader.readLine();
            }
            if (line.contains(keyWordGPS)) {
                stringBuilder.append(replacementForGPS);
                reader.readLine();
                reader.readLine();
                reader.readLine();
                reader.readLine();
                reader.readLine();
                reader.readLine();
                reader.readLine();
                reader.readLine();
            }
        }
        reader.close();
        // Создаем путь к файлу в текущей директории
        String filePath = armDirectory + pro42DirectoryProperties.TEMPLATE_FILE_SC() + satelliteId + ".txt";

        // Запись измененного содержимого обратно в файл
        File uploadFile = new File(filePath);
        FileWriter writer = new FileWriter(uploadFile);
        writer.write(stringBuilder.toString());
        writer.close();
    }

    @Override
    public void createTLEFile(String tle) throws IOException {
        String filePath = armDirectory  + File.separator + pro42DirectoryProperties.TLE();
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.writeString(path, tle, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }


    private List<String> getKeywordForSim() {
        return List.of(
                "Simulation Control",
                "Reference Orbits",
                "Spacecraft",
                "* Environment  *",
                "Ground Station",
                "Time (UTC) (Hr,Min,Sec)"
        );
    }

    private String getKeywordForFlags() {
        /*
            TRUE					!  Flag Flight Data calculate
            FALSE					!  Flag View Window calculate
            FALSE					!  Flag Sc to Gs Plan Contact calculate
            FALSE					!  Flag Sc to Gs Plan Contact calculate
            TRUE					!  Flag Out GPS
        */
        return "Flag Out GPS";
    }
}
