package cgm.controller;

import cgm.model.dto.GuestViewDto;
import cgm.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class RESTGuestsController {
    private final GuestService guestService;

    @Autowired
    public RESTGuestsController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<GuestViewDto>> getGuests(@PathVariable("id")Long id){
        return ResponseEntity.ok(this.guestService.getAllGuests(id));
    }

    @PutMapping("/edit")
    public ResponseEntity<GuestViewDto> editGuest(@RequestBody GuestViewDto guestViewDto){
        GuestViewDto guestViewDto1 = this.guestService.editGuest(guestViewDto);

        return ResponseEntity.ok(guestViewDto1);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteGuest(@PathVariable("id") Long id){

       this.guestService.deleteGuest(id);

        return ResponseEntity.ok(null);
    }

}
