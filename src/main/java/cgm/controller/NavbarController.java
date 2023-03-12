package cgm.controller;

import cgm.model.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavbarController {
    @GetMapping
    public String getNavbar(@AuthenticationPrincipal CurrentUser currentUser, Model model) {

        if(currentUser != null) {
            model.addAttribute("firstName", currentUser.getFirstName());
        }
        return "fragments/navbar";
    }

}
