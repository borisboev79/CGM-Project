package cgm.controller;


import cgm.model.dto.GroupAddDto;
import cgm.model.entity.CruiseLine;
import cgm.model.enums.Transportation;
import cgm.service.CruiseLineService;
import cgm.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final CruiseLineService cruiseLineService;



    public GroupController(GroupService groupService, CruiseLineService cruiseLineService) {
        this.groupService = groupService;
        this.cruiseLineService = cruiseLineService;
    }

    @ModelAttribute(name="groupAddDto")
    public GroupAddDto groupAddDto(){
        return new GroupAddDto();
    }

    @GetMapping("/all")
    public String getAllGroups(){
        return "groups";
    }


    @GetMapping("/add")
    public String getGroupAdd(Model model){


        List<CruiseLine> cruiselines = this.cruiseLineService.getCruiseLines();

        model.addAttribute("cruiselines", cruiselines);
        model.addAttribute("transportation", Transportation.values());

        return "group-add";
    }

    @PostMapping("/add")
    public String addGroup(@Valid @ModelAttribute(name="groupAddDto") GroupAddDto groupAddDto,
                           @AuthenticationPrincipal UserDetails userDetails,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            redirectAttributes
                    .addFlashAttribute("groupAddDto", groupAddDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.groupAddDto", bindingResult);

            return "redirect:/groups/add";
        }

        this.groupService.createGroup(groupAddDto, userDetails.getUsername());
        this.groupService.addCabins();

        return "redirect:/groups/add/cabins";
    }

    @GetMapping("/add/cabins")
    public String getCabinsAdd(){
        return "cabins-add";
    }
}
