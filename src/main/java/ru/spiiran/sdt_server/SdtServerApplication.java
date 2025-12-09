package ru.spiiran.sdt_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.spiiran.sdt_server.infrastructure.properties.Pro42DirectoryProperties;
import ru.spiiran.sdt_server.infrastructure.properties.Pro42Properties;
import ru.spiiran.sdt_server.infrastructure.properties.TLEDirectoryProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        Pro42DirectoryProperties.class,
        TLEDirectoryProperties.class,
        Pro42Properties.class
})
public class SdtServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SdtServerApplication.class, args);
    }

}
