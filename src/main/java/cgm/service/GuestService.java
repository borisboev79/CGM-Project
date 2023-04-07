package cgm.service;

import cgm.model.ObjectNotFoundException;
import cgm.model.dto.GuestAddDto;
import cgm.model.dto.GuestInitialViewDto;
import cgm.model.dto.GuestViewDto;
import cgm.model.entity.Cabin;
import cgm.model.entity.CruiseGroup;
import cgm.model.entity.Guest;
import cgm.model.enums.AgeGroup;
import cgm.repository.CabinRepository;
import cgm.repository.GroupRepository;
import cgm.repository.GuestRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
        guest.setAge(Math.abs((int) ChronoUnit.YEARS.between(LocalDate.now(), guestAddDto.getBirthDate())));
        guest.setBirthDate(dateToInstant(guestAddDto.getBirthDate()));

        if (guest.getAge() > 11) {
            guest.setAgeGroup(AgeGroup.ADULT);
        } else {
            guest.setAgeGroup(AgeGroup.CHILD);
        }


        if (cabin.getPaxNumber() <= 2) {
            cabin.setTotalPrice(cabin.getTotalPrice() + cabin.getAdultPrice());
        } else {

            if (guest.getAgeGroup().equals(AgeGroup.ADULT)) {
                cabin.setTotalPrice(cabin.getTotalPrice() + cabin.getExtraAdultPrice());
            } else {
                cabin.setTotalPrice(cabin.getTotalPrice() + cabin.getChildPrice());
            }

        }

        if (cabin.getMaxOccupancy() == cabin.getPaxNumber()) {
            cabin.setFull(true);
        }

        updateCounts(guest, group, cabin);
    }


    @Transactional
    public GuestViewDto editGuest(GuestViewDto guestViewDto) {

        Guest guest = this.guestRepository.findById(guestViewDto.getId())
                .orElseThrow(() -> new ObjectNotFoundException(guestViewDto.getId(), "guest"));

        guest.setFullName(guestViewDto.getFullName());

        LocalDate dtoBirthDate = instantToLocalDate(guestViewDto.getBirthDate());

        if (!guest.getBirthDate().equals(guestViewDto.getBirthDate())) {
            guest.setAge(Math.abs((int) ChronoUnit.YEARS.between(LocalDate.now(), dtoBirthDate)));
            guest.setBirthDate(guestViewDto.getBirthDate()
            );
        }

        guest.setEmail(guestViewDto.getEmail());
        guest.setEGN(guestViewDto.getEGN());
        guest.setPhone(guestViewDto.getPhone());
        guest.setPassportNumber(guestViewDto.getPassportNumber());

        guest = this.guestRepository.save(guest);

        return this.mapper.map(guest, GuestViewDto.class);

    }

    public void deleteGuest(Long id) {

        Guest guest = this.guestRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "guest"));
        CruiseGroup group = guest.getCabin().getCruiseGroup();
        Cabin cabin = guest.getCabin();


        if (cabin.getPaxNumber() <= 2) {
            cabin.setTotalPrice(cabin.getTotalPrice() - cabin.getAdultPrice());
        } else {

            if (guest.getAgeGroup().equals(AgeGroup.ADULT)) {
                cabin.setTotalPrice(cabin.getTotalPrice() - cabin.getExtraAdultPrice());
            } else {
                cabin.setTotalPrice(cabin.getTotalPrice() - cabin.getChildPrice());
            }
        }

        cabin.setPaxNumber(guest.getCabin().getPaxNumber() - 1);
        cabin.setAddedBy(null);


        if (cabin.isFull()) {
            cabin.setFull(false);
        }

        group.setSoldPax(group.getSoldPax() - 1);
        if (group.isSoldOut()) {
            group.setSoldOut(false);
        }

        updateCounts(guest, group, cabin);
        this.guestRepository.delete(guest);
        this.guestRepository.flush();

    }

    public List<GuestInitialViewDto> getAllGuestsInCabin(Long id){
        List<GuestInitialViewDto> cabinGuests = new ArrayList<>();
        List<Guest> persistedGuests = this.guestRepository.findAllByCabin_Id(id).orElseThrow(null)
                .stream()
                .toList();

        for (Guest persistedGuest : persistedGuests) {
            GuestInitialViewDto cabinGuest = new GuestInitialViewDto();
            this.mapper.map(persistedGuest, cabinGuest);
            cabinGuests.add(cabinGuest);

        }

        return cabinGuests;

    }


    public List<GuestViewDto> getAllGuests(Long id) {

        List<Guest> groupGuests = this.guestRepository.findAllByCabin_CruiseGroup_Id(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, "cabin"));

       return groupGuests.stream().map(guest -> {
            GuestViewDto guestView = mapper.map(guest, GuestViewDto.class);
            guestView.setCabinNumber(guest.getCabin().getId());
            return guestView;
        }).toList();
    }

    private Instant dateToInstant(LocalDate date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return date.atStartOfDay(zoneId).toInstant();
    }

    private LocalDate instantToLocalDate(Instant date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDate.ofInstant(date, zoneId);
    }

    private void updateCounts(Guest guest, CruiseGroup group, Cabin cabin) {
        this.guestRepository.saveAndFlush(guest);
        this.cabinRepository.saveAndFlush(cabin);
        this.groupRepository.saveAndFlush(group);
    }
}
