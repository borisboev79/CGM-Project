package cgm;

import cgm.model.entity.Guest;
import cgm.repository.GuestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class TestRunner implements CommandLineRunner {
    private final GuestRepository guestRepository;

    public TestRunner(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Long id = 42L;

        Guest guest = guestRepository.findById(id).orElseThrow();
        guest.setCabin(null);
        guestRepository.save(guest);

        guestRepository.delete(guest);
    }
}
