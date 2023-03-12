package cgm.controller;


import cgm.model.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {



    @GetMapping
    public String getIndex(@AuthenticationPrincipal CurrentUser currentUser, Model model) {

        if(currentUser != null){
            model.addAttribute("firstName", currentUser.getFirstName());
            model.addAttribute("lastName", currentUser.getLastName());

            model.addAttribute("branch", currentUser.getBranch());
        }

        return "index";
    }

}
