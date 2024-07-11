package fr.diginamic.springdemo.controllers;

import fr.diginamic.springdemo.exceptions.NotFoundException;
import fr.diginamic.springdemo.services.CityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TownListController {

    private final CityService cityService;

    public TownListController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/townList")
    public String townList(Model model) {
        try {
            model.addAttribute("towns", cityService.getCities());
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", "No cities found");
        }
        return "townList";
    }
}
