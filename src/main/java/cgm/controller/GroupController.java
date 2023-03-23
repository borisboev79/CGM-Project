package cgm.controller;


import cgm.model.CurrentUser;
import cgm.model.ObjectNotFoundException;
import cgm.model.dto.CabinAddDto;
import cgm.model.dto.GroupAddDto;
import cgm.model.dto.GroupViewDto;
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
import org.springframework.web.bind.annotation.*;
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

    @ModelAttribute(name = "groupAddDto")
    public GroupAddDto groupAddDto() {
        return new GroupAddDto();
    }

    @ModelAttribute(name = "cabinAddDto")
    public CabinAddDto cabinAddDto() {
        return new CabinAddDto();
    }

    @GetMapping("/all")
    public String getAllGroups(@AuthenticationPrincipal CurrentUser currentUser, Model model) {

        if (currentUser != null) {
            model.addAttribute("firstName", currentUser.getFirstName());
        }

        List<GroupViewDto> groups = this.groupService.getAllGroups();

        model.addAttribute("groups", groups);

        return "groups";
    }

    @GetMapping("/add")
    public String getGroupAdd(@AuthenticationPrincipal CurrentUser currentUser, Model model) {

        if (currentUser != null) {
            model.addAttribute("firstName", currentUser.getFirstName());
        }

        List<CruiseLine> cruiselines = this.cruiseLineService.getCruiseLines();

        model.addAttribute("cruiselines", cruiselines);
        model.addAttribute("transportation", Transportation.values());

        return "group-add";
    }

    @PostMapping("/add")
    public String addGroup(@Valid @ModelAttribute(name = "groupAddDto") GroupAddDto groupAddDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal UserDetails userDetails
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("groupAddDto", groupAddDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.groupAddDto", bindingResult);

            return "redirect:/groups/add";
        }

        String id = String.valueOf(this.groupService.createGroup(groupAddDto, userDetails.getUsername()).getId());

        return String.format("redirect:/cabins/add/%s", id);

    }

    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable Long id,
                             @AuthenticationPrincipal CurrentUser currentUser,
                             Model model) {

        if (currentUser != null) {
            model.addAttribute("firstName", currentUser.getFirstName());
        }

        GroupViewDto cruiseGroup = this.groupService.findById(id);

        if (cruiseGroup == null) {
            throw new ObjectNotFoundException(id, "group");
        }

        model.addAttribute("cruiseGroup", cruiseGroup);

        return "group-details";
    }

    @GetMapping("delete")
    public String getGroupsDelete(@AuthenticationPrincipal CurrentUser currentUser, Model model){
        if (currentUser != null) {
            model.addAttribute("firstName", currentUser.getFirstName());
        }

        List<GroupViewDto> groups = this.groupService.getAllGroups();

        model.addAttribute("groups", groups);

        return "groups-delete";
    }

    @GetMapping("delete/{id}")
    public String deleteGroup(@PathVariable Long id){

      this.groupService.deleteGroupById(id);

        return "redirect:/groups/delete";
    }
}
