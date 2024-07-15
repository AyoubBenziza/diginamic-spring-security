package fr.diginamic.springdemo.controllers.views;

import fr.diginamic.springdemo.exceptions.NotFoundException;
import fr.diginamic.springdemo.services.DepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DepartmentViewController {

    private final DepartmentService departmentService;

    public DepartmentViewController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/departments")
    public String departmentList(Model model, HttpServletRequest request) {
        try {
            model.addAttribute("departments", departmentService.getDepartments());
            model.addAttribute("requestURI", request.getRequestURI());
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", "No departments found");
        }
        return "department/departmentList";
    }

    @GetMapping("/departments/delete/{code}")
    public String deleteDepartment(@PathVariable("code") String code, Model model) {
        try {
            departmentService.delete(code);
            model.addAttribute("departments", departmentService.getDepartments());
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", "No departments found");
        }
        // Redirect to the departments listing page after deletion
        return "redirect:/departments";
    }
}
