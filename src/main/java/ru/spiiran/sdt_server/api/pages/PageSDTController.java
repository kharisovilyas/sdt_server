package ru.spiiran.sdt_server.api.pages;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class PageSDTController {
    @GetMapping("/login")
    public String login() {
        return "login"; // Возвращает login.html
    }

    @GetMapping("/sdt")
    public String sdtPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // Получаем первую роль пользователя (упрощенный пример)
            String userRole = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER"); // Роль по умолчанию, если что-то пошло не так

            // Убираем префикс "ROLE_" и приводим к нижнему регистру для соответствия вашему JS коду
            userRole = userRole.replace("ROLE_", "").toLowerCase();

            // Добавляем роль в модель, чтобы Thymeleaf мог ее использовать
            model.addAttribute("userRole", userRole);
        }
        return "sdt"; // Возвращает sdt.html
    }
}
