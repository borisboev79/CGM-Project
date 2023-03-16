package cgm.controller;

import cgm.model.dto.GuestViewDto;
import cgm.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class ShowAllPaxController {
    private final GuestService guestService;

    @Autowired
    public ShowAllPaxController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<GuestViewDto>> getGuests(@PathVariable("id")Long id){
        return ResponseEntity.ok(this.guestService.getAllGuests(id));
    }

}
