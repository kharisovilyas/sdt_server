package ru.spiiran.sdt_server.api.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageTLEController {

    @GetMapping("/tle")
    public String getTlePage() {
        return "tle"; // имя thymeleaf-шаблона tle.html
    }
}
