package ru.spiiran.sdt_server.infrastructure.client.pro42;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.infrastructure.dto.DtoPro42Coordinate;
import ru.spiiran.sdt_server.infrastructure.file.pro42.Pro42DirectoryControl;
import ru.spiiran.sdt_server.infrastructure.file.pro42.ReadingPro42OutputFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RunPro42Impl implements RunPro42 {

    private static final Logger logger = LoggerFactory.getLogger(RunPro42Impl.class);
    private final Pro42DirectoryControl pro42DirectoryControl;
    private final ReadingPro42OutputFile readingPro42OutputFile;
    private String pro42RunDirectory;
    private String pro42ReposDirectory;
    private String pro42CommandDirectory;
    private String outputFileName;

    public RunPro42Impl(Pro42DirectoryControl pro42DirectoryControl, ReadingPro42OutputFile readingPro42OutputFile) {
        this.pro42DirectoryControl = pro42DirectoryControl;
        this.readingPro42OutputFile = readingPro42OutputFile;
    }

    @Override
    public void init(String username) throws IOException, InterruptedException {
        this.pro42CommandDirectory = pro42DirectoryControl.getDirectoryForCommand();
        this.pro42ReposDirectory = pro42DirectoryControl.getPro42Directory().toString();
        this.pro42RunDirectory = pro42DirectoryControl.getRunDirectory();
        this.outputFileName = pro42DirectoryControl.getOutputFileName();

        logger.info("Pro42 initialization completed: [CommandDir: {}] [RunDir: {}] [Output: {}]",
                pro42CommandDirectory, pro42RunDirectory, outputFileName);
    }

    @Override
    public List<DtoPro42Coordinate> copyResponsePro42() throws IOException, InterruptedException {
        final long startTime = System.currentTimeMillis();
        final List<String> command = List.of(
                "cd " + this.pro42RunDirectory,
                "./42-Complex " + this.pro42CommandDirectory
        );

        logger.debug("Building Pro42 command sequence");
        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", String.join("\n", command));
        processBuilder.redirectErrorStream(true);

        logger.info("Starting Pro42 execution: [WorkDir: {}]", pro42RunDirectory);
        final Process process = processBuilder.start();

        logger.debug("Process started with PID: {}", process.pid());

        // Silent console output handling
        Thread outputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                while (reader.readLine() != null) {
                    // Intentional empty - consume output without logging
                }
            } catch (IOException e) {
                logger.error("Process output stream failure: {}", e.getMessage());
            }
        });
        outputThread.start();

        // Process termination handling
        boolean normalExit = process.waitFor(60, TimeUnit.MINUTES);
        final long executionTime = System.currentTimeMillis() - startTime;

        if (!normalExit) {
            process.destroy();
            logger.error("Process timeout after {} ms", executionTime);
            throw new IllegalStateException("Pro42 execution timeout");
        }

        outputThread.join();
        final int exitCode = process.exitValue();

        logger.info("Process completed: [Time: {} ms] [ExitCode: {}]", executionTime, exitCode);

        if (exitCode != 0) {
            logger.warn("Non-zero exit code detected");
            throw new RuntimeException("Pro42 exited with code: " + exitCode);
        }

        final Path outputPath = Paths.get(pro42ReposDirectory, outputFileName);
        logger.debug("Parsing output file: {}", outputPath);

        return readingPro42OutputFile.readingOutputFile(outputPath);
    }
}
