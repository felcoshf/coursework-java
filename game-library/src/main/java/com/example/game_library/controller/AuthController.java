package com.example.game_library.controller;

import com.example.game_library.model.User;
import com.example.game_library.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    public static final String SESSION_USER_ID = "userId";

    private final UserService userService;

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String login,
                           @RequestParam String password,
                           Model model) {
        if (login.isBlank() || password.isBlank()) {
            model.addAttribute("error", "Заполните все поля");
            return "register";
        }
        if (!userService.register(login, password)) {
            model.addAttribute("error", "Логин уже занят");
            return "register";
        }
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String login,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        User user = userService.authenticate(login, password);
        if (user == null) {
            model.addAttribute("error", "Неверный логин или пароль");
            return "login";
        }
        session.setAttribute(SESSION_USER_ID, user.getId());
        return "redirect:/games";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/games";
    }
}
