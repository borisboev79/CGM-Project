package cgm.controller;


import cgm.model.GrouManUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {



    @GetMapping
    public String getIndex(@AuthenticationPrincipal GrouManUser grouManUser, Model model) {

        if(grouManUser != null){
            model.addAttribute("firstName", grouManUser.getFirstName());
            model.addAttribute("lastName", grouManUser.getLastName());

            model.addAttribute("branch", grouManUser.getBranch());
        }

        return "index";
    }

}
