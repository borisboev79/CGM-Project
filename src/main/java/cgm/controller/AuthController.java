package cgm.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/register")
    private String register() {
        return "auth-register";
    }

    @GetMapping("/login")
    private String login() {
        return "auth-login";
    }

    @GetMapping("/user")
    public String getUser(Principal principal){
        return principal.getName();
    }

    @PostMapping("/logout")
    private String logout() {
        return "index";
    }
}
