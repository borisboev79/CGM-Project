package cgm.controller;


import cgm.model.dto.CabinAddDto;
import cgm.model.dto.GroupAddDto;
import cgm.model.entity.CruiseGroup;
import cgm.model.entity.CruiseLine;
import cgm.model.enums.Transportation;
import cgm.service.CabinService;
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
    private final CabinService cabinService;
    private final CruiseLineService cruiseLineService;

    public GroupController(GroupService groupService, CabinService cabinService, CruiseLineService cruiseLineService) {
        this.groupService = groupService;
        this.cabinService = cabinService;
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
    public String getAllGroups(Model model) {

        List<CruiseGroup> groups = this.groupService.getAllGroups();

        model.addAttribute("groups", groups);

        return "groups";
    }

    @GetMapping("/add")
    public String getGroupAdd(Model model) {


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

        return String.format("redirect:/groups/add/cabins/%s", id);

    }

    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable Long id, Model model) {
        CruiseGroup cruiseGroup = this.groupService.findById(id);

        model.addAttribute("cruiseGroup", cruiseGroup);

        return "details";
    }

    @GetMapping("/add/cabins/{id}")
    public String getCabinsAdd(@PathVariable Long id, Model model) {
        CruiseGroup currentGroup = this.groupService.findById(id);

        model.addAttribute("currentGroup", currentGroup);

        return "cabins-add";
    }

    @PostMapping("/add/cabins")
    public String getCabinsAdd(@Valid @ModelAttribute(name = "cabinAddDto") CabinAddDto cabinAddDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal UserDetails userDetails
    ) {


        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("cabinAddDto", cabinAddDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.cabinAddDto", bindingResult);

            return "redirect:/groups/add/cabins";
        }


        this.cabinService.addCabin(cabinAddDto);

        return "redirect:/groups/all";

    }
}
