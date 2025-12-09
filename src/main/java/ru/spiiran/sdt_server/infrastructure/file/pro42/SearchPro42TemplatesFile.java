package ru.spiiran.sdt_server.infrastructure.file.pro42;

import java.io.File;

public interface SearchPro42TemplatesFile {
    String fileExtension = ".txt";
    File searchFileSim();
    File searchFileOrb();
    File searchFileSc();
    File searchFileFlag();
}
