package cgm.controller;

import cgm.model.dto.GuestAddDto;
import cgm.model.entity.Cabin;
import cgm.service.CabinService;
import cgm.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
                              Model model) {

        final Cabin cabin = this.cabinService.findById(id);

        model.addAttribute("cabin", cabin);

        return "guest-add";
    }

    @PostMapping("/add/{id}")
    public String getGuest(@PathVariable Long id,
                           @Valid @ModelAttribute(name = "guestAddDto") GuestAddDto guestAddDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes
                          ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("guestAddDto", guestAddDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.guestAddDto", bindingResult);

            return "redirect:/guests/add/{id}";
        }

        this.guestService.addGuest(guestAddDto, id);


        return "redirect:/groups/all";
    }
}
