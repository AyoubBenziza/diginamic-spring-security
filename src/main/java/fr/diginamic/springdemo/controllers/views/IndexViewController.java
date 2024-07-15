package fr.diginamic.springdemo.controllers.views;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class IndexViewController {
    @GetMapping()
    public String index(Model model, Authentication auth, HttpServletRequest request) {
        model.addAttribute("auth", auth);
        model.addAttribute("requestURI", request.getRequestURI());
        return "index";
    }
}
