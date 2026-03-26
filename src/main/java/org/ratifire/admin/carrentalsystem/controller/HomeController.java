package org.ratifire.admin.carrentalsystem.controller;

import org.ratifire.admin.carrentalsystem.enums.CarType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("carTypes", CarType.values());
        return "index";
    }
}
