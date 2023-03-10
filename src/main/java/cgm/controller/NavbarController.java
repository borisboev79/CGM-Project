package cgm.controller;

import cgm.model.GrouManUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavbarController {
    @GetMapping
    public String getNavbar(@AuthenticationPrincipal GrouManUser grouManUser, Model model) {

        if(grouManUser != null) {
            model.addAttribute("firstName", grouManUser.getFirstName());
        }
        return "fragments/navbar";
    }

}
