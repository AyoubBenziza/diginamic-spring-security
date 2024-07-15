package fr.diginamic.springdemo.controllers.views;

import fr.diginamic.springdemo.exceptions.NotFoundException;
import fr.diginamic.springdemo.services.CityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TownViewController {

    private final CityService cityService;

    public TownViewController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/towns")
    public String townList(Model model, Authentication auth, HttpServletRequest request) {
        try {
            model.addAttribute("towns", cityService.getCities());
            model.addAttribute("requestURI", request.getRequestURI());
            model.addAttribute("auth", auth);
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", "No cities found");
        }
        return "town/townList";
    }

    @GetMapping("/towns/delete/{id}")
    public String deleteTown(@PathVariable("id") int id, Model model) {
        try {
            cityService.delete(id);
            model.addAttribute("towns", cityService.getCities());
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", "No cities found");
        }
        // Redirect to the towns listing page after deletion
        return "redirect:/towns";
    }
}
