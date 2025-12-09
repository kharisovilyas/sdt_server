package ru.spiiran.sdt_server.api.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageVBController {
    @GetMapping("/vb")
    public String getTlePage() {
        return "vb_ballistic"; // имя thymeleaf-шаблона tle.html
    }
}
