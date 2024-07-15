package fr.diginamic.springdemo.controllers.views;

import fr.diginamic.springdemo.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthViewController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "auth/logout";
    }

    @GetMapping("/register")
    public String register() { return "auth/register"; }

    @PostMapping("/register")
    public String registerPost(@RequestParam("username") String username, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        try {
            userAccountService.registerUser(username, password);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
        return "redirect:/login";
    }
}
