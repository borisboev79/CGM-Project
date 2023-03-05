package cgm.service;

import cgm.model.dto.GuestAddDto;
import cgm.model.entity.Cabin;
import cgm.model.entity.CruiseGroup;
import cgm.model.entity.Guest;
import cgm.repository.CabinRepository;
import cgm.repository.GroupRepository;
import cgm.repository.GuestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class GuestService {
    private final GuestRepository guestRepository;
    private final CabinRepository cabinRepository;
    private final GroupRepository groupRepository;
    private final ModelMapper mapper;

    @Autowired
    public GuestService(GuestRepository guestRepository, CabinRepository cabinRepository, GroupRepository groupRepository, ModelMapper mapper) {
        this.guestRepository = guestRepository;
        this.cabinRepository = cabinRepository;
        this.groupRepository = groupRepository;

        this.mapper = mapper;
    }

    public void addGuest(GuestAddDto guestAddDto, Long id) {
        Cabin cabin = this.cabinRepository.findById(id).orElseThrow();
        CruiseGroup group = this.groupRepository.findById(cabin.getCruiseGroup().getId()).orElseThrow();

        Guest guest = this.mapper.map(guestAddDto, Guest.class);
        guest.setCabin(cabin);
        guest.setAge(LocalDate.now().getYear() - guestAddDto.getBirthDate().getYear());
        guest.setBirthDate(dateToInstant(guestAddDto.getBirthDate()));

        this.guestRepository.saveAndFlush(guest);

        cabin.setCount(cabin.getCount() -1);
        group.setTotalPax(group.getTotalPax() - 1);

        System.out.println(false);

    }

    private Instant dateToInstant(LocalDate date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return date.plusDays(1).atStartOfDay(zoneId).toInstant();
    }

}
