package cgm.controller;

import cgm.model.dto.GuestViewDto;
import cgm.service.GuestService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/edit")
    public ResponseEntity<GuestViewDto> editGuest(@RequestBody GuestViewDto guestViewDto){
        return ResponseEntity.ok(this.guestService.editGuest(guestViewDto));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteGuest(@PathVariable("id") Long id, @RequestBody GuestViewDto guestViewDto){

       this.guestService.deleteGuest(guestViewDto);

        return "redirect:/api/guests/" + id;
    }

}
