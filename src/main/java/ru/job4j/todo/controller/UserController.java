package ru.job4j.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        List<String> zones = new ArrayList<>(ZoneId.getAvailableZoneIds());
        Collections.sort(zones); // опционально — для удобства сортировки
        model.addAttribute("zones", zones);
        model.addAttribute("user", new User()); // если используешь th:object в форме
        return "users/register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute User user, @RequestParam("timeZone") String timeZone) {
        user.setTimeZone(timeZone);
        var savedUser = userService.save(user);
        if (savedUser.isEmpty()) {
            model.addAttribute("message", "Пользователь с такой почтой уже существует");
            return "errors/404";
        }
        return "redirect:/users/register";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
        var userOption = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (userOption.isEmpty()) {
            model.addAttribute("error", "Почта или пароль введены неверно");
            return "users/login";
        }
        var session = request.getSession();
        session.setAttribute("user", userOption.get());
        return "redirect:/tasks";
    }
}
