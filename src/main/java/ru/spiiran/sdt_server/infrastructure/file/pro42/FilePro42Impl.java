package ru.spiiran.sdt_server.infrastructure.file.pro42;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class FilePro42Impl implements FilePro42 {
    private final GenericPro42Files genericPro42Files;
    private final Pro42DirectoryControl pro42DirectoryControl;

    public FilePro42Impl(GenericPro42Files genericPro42Files, Pro42DirectoryControl pro42DirectoryControl) {
        this.genericPro42Files = genericPro42Files;
        this.pro42DirectoryControl = pro42DirectoryControl;
    }

    @Override
    public void genericPro42Files(Long satelliteId, String tle) throws IOException {
        Path armDirectory = pro42DirectoryControl.getArmDirectory();
        genericPro42Files.init(armDirectory);
        genericPro42Files.createTLEFile(tle);
        genericPro42Files.createSimFile(satelliteId);
        genericPro42Files.createOrbFiles(tle, satelliteId);
        genericPro42Files.createScFiles(satelliteId);
        genericPro42Files.createCsgFlagFile();
    }
}
