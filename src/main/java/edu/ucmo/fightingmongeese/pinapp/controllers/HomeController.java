package edu.ucmo.fightingmongeese.pinapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for routing to base pages of the demo web page.
 * Currently just redirects unauthenticated users to the login page and
 * authenticated users to the pin database table page.
 */
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
