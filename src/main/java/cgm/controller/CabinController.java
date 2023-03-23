package cgm.controller;

import cgm.model.CurrentUser;
import cgm.model.ObjectNotFoundException;
import cgm.model.dto.CabinAddDto;
import cgm.model.dto.GroupViewDto;
import cgm.model.entity.Cabin;
import cgm.model.enums.CabinType;
import cgm.service.CabinService;
import cgm.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cabins")
public class CabinController {
    private final CabinService cabinService;
    private final GroupService groupService;

    @Autowired
    public CabinController(CabinService cabinService, GroupService groupService) {
        this.cabinService = cabinService;
        this.groupService = groupService;
    }

    @ModelAttribute(name = "cabinAddDto")
    public CabinAddDto cabinAddDto() {
        return new CabinAddDto();
    }


    @GetMapping("/add/{id}")
    public String getCabinsAdd(@PathVariable Long id,
                               @AuthenticationPrincipal CurrentUser currentUser,
                               Model model) {


        model.addAttribute("firstName", currentUser.getFirstName());

        GroupViewDto currentGroup = this.groupService.findById(id);

        if (currentGroup == null) {
            throw new ObjectNotFoundException(id, "group");
        }

        model.addAttribute("currentGroup", currentGroup);
        model.addAttribute("cabinType", CabinType.values());

        return "cabins-add";
    }


    @PostMapping("/add/{id}")
    public String addCabins(@Valid @ModelAttribute(name = "cabinAddDto") CabinAddDto cabinAddDto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            @PathVariable Long id) {

        GroupViewDto currentGroup = this.groupService.findById(id);

        if (currentGroup == null) {
            throw new ObjectNotFoundException(id, "group");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("cabinAddDto", cabinAddDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.cabinAddDto", bindingResult);

            return String.format("redirect:/groups/add/cabins/%s", id);
        }


        this.cabinService.addCabin(cabinAddDto, id);

        return "redirect:/groups/details/" + id;

    }



    @GetMapping("/close/{id}")
    public String closeCabin(@PathVariable Long id, @AuthenticationPrincipal CurrentUser currentUser) {

        final Cabin cabinToClose = this.cabinService.findById(id);

        if(cabinToClose == null){
            throw new ObjectNotFoundException(id, "cabin");
        }

        Long groupId = cabinToClose.getCruiseGroup().getId();

        this.cabinService.closeCabin(cabinToClose, currentUser.getUsername());

        this.groupService.checkAvailability(cabinToClose.getCruiseGroup());

        return "redirect:/groups/details/" + groupId;
    }
}
