package ru.advisio.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.advisio.core.services.KeycloakService;

@Controller
public class AuthController {

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("token", "");
        model.addAttribute("error", null);
        return "home";
    }

    @PostMapping("/get-token")
    public String getToken(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        try {
            String token = KeycloakService.getToken(username, password);
            model.addAttribute("token", token);
            model.addAttribute("error", null);
        } catch (Exception e) {
            model.addAttribute("token", "");
            model.addAttribute("error", "Ошибка получения токена: " + e.getMessage());
        }

        return "home";
    }
}
