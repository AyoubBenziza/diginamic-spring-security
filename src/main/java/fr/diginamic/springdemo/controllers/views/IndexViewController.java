package fr.diginamic.springdemo.controllers.views;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class IndexViewController {
    @GetMapping()
    public String index(Model model, Authentication auth) {
        model.addAttribute("auth", auth);
        return "index";
    }
}
