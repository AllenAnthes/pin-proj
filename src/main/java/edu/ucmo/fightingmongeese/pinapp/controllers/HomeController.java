package edu.ucmo.fightingmongeese.pinapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
//        return "home";
        return "redirect:/pins/list";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
