package ru.spiiran.sdt_server.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pro42.files")
public record Pro42DirectoryProperties(
        String TEMPLATE_DIR,
        String ARM_DIR,
        String REPOS_DIRECTORY,
        String RUN_DIRECTORY,
        String COMMAND_DIRECTORY,
        String GPS_REPOS,
        String TLE,
        String TEMPLATE_FILE_SIM,
        String TEMPLATE_FILE_ORB,
        String TEMPLATE_FILE_SC,
        String TEMPLATE_FILE_FLAG
) {

}
