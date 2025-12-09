package ru.spiiran.sdt_server.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tle")
public record TLEDirectoryProperties (
    String INPUT_DIRECTORY
){

}
