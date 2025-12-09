package ru.spiiran.sdt_server.infrastructure.file.pro42;

import org.springframework.stereotype.Component;
import ru.spiiran.sdt_server.infrastructure.properties.Pro42DirectoryProperties;

import java.io.File;
import java.nio.file.Paths;

@Component
public class SearchPro42TemplatesFileImpl implements SearchPro42TemplatesFile {
    private final Pro42DirectoryProperties pro42DirectoryProperties;

    public SearchPro42TemplatesFileImpl(Pro42DirectoryProperties pro42DirectoryProperties) {
        this.pro42DirectoryProperties = pro42DirectoryProperties;
    }

    @Override
    public File searchFileSim() {
        return Paths.get(pro42DirectoryProperties.TEMPLATE_DIR(), pro42DirectoryProperties.TEMPLATE_FILE_SIM()).toFile();
    }

    @Override
    public File searchFileOrb() {
        return Paths.get(pro42DirectoryProperties.TEMPLATE_DIR(), pro42DirectoryProperties.TEMPLATE_FILE_ORB() + fileExtension).toFile();
    }

    @Override
    public File searchFileSc() {
        return Paths.get(pro42DirectoryProperties.TEMPLATE_DIR(), pro42DirectoryProperties.TEMPLATE_FILE_SC() + fileExtension).toFile();
    }

    @Override
    public File searchFileFlag() {
        return Paths.get(pro42DirectoryProperties.TEMPLATE_DIR(), pro42DirectoryProperties.TEMPLATE_FILE_FLAG()).toFile();
    }
}
