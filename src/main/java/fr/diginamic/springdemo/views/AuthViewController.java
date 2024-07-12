package fr.diginamic.springdemo.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthViewController {
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "auth/logout";
    }
}
