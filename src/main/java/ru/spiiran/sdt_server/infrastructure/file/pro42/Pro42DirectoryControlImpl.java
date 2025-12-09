package ru.spiiran.sdt_server.infrastructure.file.pro42;

import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.infrastructure.properties.Pro42DirectoryProperties;
import ru.spiiran.sdt_server.util.directory.DirectoryFileControl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class Pro42DirectoryControlImpl implements Pro42DirectoryControl {
    private final DirectoryFileControl directoryFileControl;
    private final Pro42DirectoryProperties pro42DirectoryData;
    private Path pro42Directory;
    private Path armDirectory;
    private String workplacePath;

    public Pro42DirectoryControlImpl(
            DirectoryFileControl directoryFileControl,
            Pro42DirectoryProperties pro42DirectoryData
    ) {
        this.directoryFileControl = directoryFileControl;
        this.pro42DirectoryData = pro42DirectoryData;
    }

    /**
     * Инициализируем workplacePath - поддиректория с именем пользователя ....../ilyas
     * armDirectory - директория АРМ - поддиректория в сервере /src/static/Ballistic/GPS/ilyas
     * pro42Directory - директория Pro42 - поддиректория в Pro42 ../Ballistic/GPS/ilyas
     * */
    @Override
    public void init(String username) {
        this.workplacePath = pro42DirectoryData.GPS_REPOS() + File.separator + username;
        this.armDirectory = getArmDirectory();
        this.pro42Directory = getPro42Directory();
    }

    @Override
    public Path getArmDirectory() {
        return Paths.get(
                pro42DirectoryData.ARM_DIR() +
                        File.separator +
                        workplacePath
        );
    }

    @Override
    public Path getPro42Directory() {
        return Paths.get(
                pro42DirectoryData.REPOS_DIRECTORY() +
                        File.separator +
                        workplacePath
        );
    }

    @Override
    public void createDirectoriesIfNotExist() throws IOException {
        directoryFileControl.createDirectoryIfNotExist(pro42Directory);
        directoryFileControl.createDirectoryIfNotExist(armDirectory);
    }

    @Override
    public void clearPro42Directory() throws IOException {
        directoryFileControl.clearDirectory(getPro42Directory());
    }

    @Override
    public void moveFilesToBallistic() throws IOException {
        directoryFileControl.moveFilesTo(pro42Directory, armDirectory);
    }

    @Override
    public String getDirectoryForCommand() {
        return pro42DirectoryData.COMMAND_DIRECTORY() + workplacePath;
    }

    @Override
    public String getRunDirectory() {
        return pro42DirectoryData.RUN_DIRECTORY();
    }

    @Override
    public String getOutputFileName() {
        return File.separator + "csgGpsSc0";
    }
}
