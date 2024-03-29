package cgm.controller;

import cgm.model.CurrentUser;
import cgm.model.ObjectNotFoundException;
import cgm.model.dto.GuestAddDto;
import cgm.model.entity.Cabin;
import cgm.model.entity.UserEntity;
import cgm.service.CabinService;
import cgm.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guests")
public class GuestController {
    private final GuestService guestService;
    private final CabinService cabinService;

    @ModelAttribute(name = "guestAddDto")
    public GuestAddDto guestAddDto() {
        return new GuestAddDto();
    }

    @Autowired
    public GuestController(GuestService guestService, CabinService cabinService) {
        this.guestService = guestService;
        this.cabinService = cabinService;
    }

    @GetMapping("/add/{id}")
    public String getAddGuest(@PathVariable Long id,
                              @AuthenticationPrincipal CurrentUser currentUser,
                              Model model) {

        final Cabin cabin = this.cabinService.findById(id);

        if(currentUser != null){
            model.addAttribute("firstName", currentUser.getFirstName());
        }


        if(cabin == null){
            throw new ObjectNotFoundException(id, "cabin");
        }

        model.addAttribute("cabin", cabin);
        model.addAttribute("guests", this.guestService.getAllGuestsInCabin(id));
        model.addAttribute("groupId", id);


        return "guest-add";
    }

    @PostMapping("/add/{id}")
    public String getGuest(@PathVariable Long id,
                           @Valid @ModelAttribute(name = "guestAddDto") GuestAddDto guestAddDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal CurrentUser currentUser,
                           Model model
                          ) {

        model.addAttribute("firstName", currentUser.getFirstName());


        final Cabin cabin = this.cabinService.findById(id);

        if(cabin == null){
            throw new ObjectNotFoundException(id, "cabin");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("guestAddDto", guestAddDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.guestAddDto", bindingResult);

            return "redirect:/guests/add/{id}";
        }

        cabin.setAddedBy(currentUser.getBranch());

        this.guestService.addGuest(guestAddDto, id);

        Long groupId = cabin.getCruiseGroup().getId();

        if(cabin.isFull()){
            return "redirect:/groups/details/" + groupId;
        }
        return "redirect:/guests/add/" + id;
    }

    @GetMapping("/group/{groupId}/delete/{id}")
    public String deleteGuest(@PathVariable Long groupId, @PathVariable Long id) {

        this.guestService.deleteGuest(id);

        return "redirect:/guests/add/" + groupId;
    }

}
