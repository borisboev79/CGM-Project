package cgm.controller;

import cgm.model.ObjectNotFoundException;
import cgm.model.entity.Cabin;
import cgm.service.CabinService;
import cgm.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("cabin/")
public class CabinController {
    private final CabinService cabinService;
    private final GroupService groupService;

    @Autowired
    public CabinController(CabinService cabinService, GroupService groupService) {
        this.cabinService = cabinService;
        this.groupService = groupService;
    }

    @GetMapping("/close/{id}")
    public String closeCabin(@PathVariable Long id) {

        final Cabin cabinToClose = this.cabinService.findById(id);

        if(cabinToClose == null){
            throw new ObjectNotFoundException(id, "cabin");
        }

        Long groupId = cabinToClose.getCruiseGroup().getId();

        this.cabinService.closeCabin(cabinToClose);

        this.groupService.checkAvailability(cabinToClose.getCruiseGroup());




        return "redirect:/groups/details/" + groupId;
    }
}
