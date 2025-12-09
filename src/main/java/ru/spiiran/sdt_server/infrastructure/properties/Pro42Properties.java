package ru.spiiran.sdt_server.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pro42")
public record Pro42Properties (
        Long DURATION,
        Double STEP,
        Long CONVERT_TO_SEC
){
}
