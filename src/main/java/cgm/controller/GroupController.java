package cgm.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/groups")
public class GroupController {

    @GetMapping("/all")
    public String getAllGroups(){
        return "groups";
    }
}
