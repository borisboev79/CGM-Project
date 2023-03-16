package cgm.service;

import cgm.model.dto.GuestAddDto;
import cgm.model.dto.GuestViewDto;
import cgm.model.entity.Cabin;
import cgm.model.entity.CruiseGroup;
import cgm.model.entity.Guest;
import cgm.model.enums.AgeGroup;
import cgm.repository.CabinRepository;
import cgm.repository.GroupRepository;
import cgm.repository.GuestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

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
        guest.getCabin().setPaxNumber(cabin.getPaxNumber() + 1);
        group.setSoldPax(group.getSoldPax() + 1);
        guest.setAge(Math.abs((int)ChronoUnit.YEARS.between(LocalDate.now(), guestAddDto.getBirthDate())));
        guest.setBirthDate(dateToInstant(guestAddDto.getBirthDate()));

        if(guest.getAge() > 11){
            guest.setAgeGroup(AgeGroup.ADULT);
        } else {
            guest.setAgeGroup(AgeGroup.CHILD);
        }

        if(cabin.getPaxNumber() < 2) {
            cabin.setTotalPrice(cabin.getTotalPrice() + cabin.getAdultPrice());
        } else {

            if (guest.getAgeGroup().equals(AgeGroup.ADULT)) {
                cabin.setTotalPrice(cabin.getTotalPrice() + cabin.getExtraAdultPrice());
            } else {
                cabin.setTotalPrice(cabin.getTotalPrice() + cabin.getChildPrice());
            }

        }

        if(cabin.getMaxOccupancy() == cabin.getPaxNumber()){
            cabin.setFull(true);
        }

        this.guestRepository.saveAndFlush(guest);
        this.cabinRepository.save(cabin);
        this.groupRepository.save(group);



    }

    private Instant dateToInstant(LocalDate date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return date.plusDays(1).atStartOfDay(zoneId).toInstant();
    }

    public List<GuestViewDto> getAllGuests(Long id) {

        return Objects.requireNonNull(this.guestRepository.findAllByCabin_CruiseGroup_Id(id).orElse(null))
                .stream()
                .map(guest -> mapper.map(guest, GuestViewDto.class)).toList();

    }



}
