package cgm.controller;


import cgm.service.GroupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public String getAllGroups(){
        return "groups";
    }

    @PostMapping("/add")
    public String addGroup(){
        this.groupService.createGroup();

        return "redirect:/groups/all";
    }
}
