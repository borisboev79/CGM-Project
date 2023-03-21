package cgm.service;

import cgm.model.ObjectNotFoundException;
import cgm.model.dto.GuestAddDto;
import cgm.model.dto.GuestViewDto;
import cgm.model.entity.Cabin;
import cgm.model.entity.CruiseGroup;
import cgm.model.entity.Guest;
import cgm.model.enums.AgeGroup;
import cgm.repository.CabinRepository;
import cgm.repository.GroupRepository;
import cgm.repository.GuestRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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


        if (cabin.getPaxNumber() < 2) {
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

    public List<GuestViewDto> getAllGuests(Long id) {

        List<GuestViewDto> guestViews = new ArrayList<>();

        List<Guest> groupGuests = this.guestRepository.findAllByCabin_CruiseGroup_Id(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, "cabin"))
                .stream().toList();

        for (Guest groupGuest : groupGuests) {
            GuestViewDto guestView = mapper.map(groupGuest, GuestViewDto.class);
            guestView.setCabinNumber(groupGuest.getCabin().getId());
            guestViews.add(guestView);
        }


        return guestViews;
    }

    @Transactional
    public GuestViewDto editGuest(GuestViewDto guestViewDto) {

        String dtoFullName = guestViewDto.getFullName();
        String dtoEmail = guestViewDto.getEmail();
        String dtoPhone = guestViewDto.getPhone();
        String dtoEgn = guestViewDto.getEGN();
        String dtoPassport = guestViewDto.getPassportNumber();
        LocalDate dtoBirthDate = instantToLocalDate(guestViewDto.getBirthDate());


        Guest guest = this.guestRepository.findById(guestViewDto.getId())
                .orElseThrow(() -> new ObjectNotFoundException(guestViewDto.getId(), "guest"));


        if (!guest.getFullName().equals(dtoFullName)) {
            guest.setFullName(dtoFullName);
        }
        if (!guest.getBirthDate().equals(guestViewDto.getBirthDate())) {
            guest.setAge(Math.abs((int) ChronoUnit.YEARS.between(LocalDate.now(), dtoBirthDate)));
            guest.setBirthDate(guestViewDto.getBirthDate()
                    .plus(1, ChronoUnit.DAYS)
            );

        }
        if (!guest.getEmail().equals(dtoEmail)) {
            guest.setEmail(dtoEmail);
        }
        if (!guest.getEGN().equals(dtoEgn)) {
            guest.setEGN(dtoEgn);
        }
        if (!guest.getPhone().equals(dtoPhone)) {
            guest.setPhone(dtoPhone);
        }
        if (!guest.getPassportNumber().equals(dtoPassport)) {
            guest.setPassportNumber(dtoPassport);
        }

        guest = this.guestRepository.save(guest);

        return this.mapper.map(guest, GuestViewDto.class);

    }

    public void deleteGuest(Long id) {

        Guest guest = this.guestRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
        CruiseGroup group = guest.getCabin().getCruiseGroup();
        Cabin cabin = guest.getCabin();


        cabin.setPaxNumber(guest.getCabin().getPaxNumber() - 1);
        if (cabin.isFull()) {
            cabin.setFull(false);
        }

        guest.setCabin(null);

        group.setSoldPax(group.getSoldPax() - 1);
        if (group.isSoldOut()) {
            group.setSoldOut(false);
        }

        updateCounts(guest, group, cabin);
        this.guestRepository.delete(guest);
        this.guestRepository.flush();

    }

    private Instant dateToInstant(LocalDate date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return date.plusDays(1).atStartOfDay(zoneId).toInstant();
    }

    private LocalDate instantToLocalDate(Instant date){
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDate.ofInstant(date, zoneId);
    }

    private void updateCounts(Guest guest, CruiseGroup group, Cabin cabin) {
        this.guestRepository.saveAndFlush(guest);
        this.cabinRepository.saveAndFlush(cabin);
        this.groupRepository.saveAndFlush(group);
    }
}
