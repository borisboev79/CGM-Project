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
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

        this.guestRepository.saveAndFlush(guest);
        updateCounts(group, cabin);
    }

    @Transactional
    public void deleteGuest(GuestViewDto guestViewDto) {

        Guest guest = this.guestRepository.findById(guestViewDto.getId()).orElseThrow(ObjectNotFoundException::new);
        CruiseGroup group = guest.getCabin().getCruiseGroup();
        Cabin cabin = guest.getCabin();

        cabin.setPaxNumber(guest.getCabin().getPaxNumber() - 1);
        group.setSoldPax(group.getSoldPax() - 1);

        updateCounts(group, cabin);
        guestRepository.delete(guest);



    }



    public List<GuestViewDto> getAllGuests(Long id) {

        return Objects.requireNonNull(this.guestRepository.findAllByCabin_CruiseGroup_Id(id).orElse(null))
                .stream()
                .map(guest -> mapper.map(guest, GuestViewDto.class)).toList();
    }


    private void updateCounts(CruiseGroup group, Cabin cabin){
        this.cabinRepository.save(cabin);
        this.groupRepository.save(group);
    }

    @Transactional
    public GuestViewDto editGuest(GuestViewDto guestViewDto) {

        String dtoFullName = guestViewDto.getFullName();
        String dtoEmail = guestViewDto.getEmail();
        String dtoPhone = guestViewDto.getPhone();
        String dtoEgn = guestViewDto.getEGN();
        String dtoPassport = guestViewDto.getPassportNumber();
        Instant dtoBirthDate = guestViewDto.getBirthDate();


        Guest guest = this.guestRepository.findById(guestViewDto.getId()).orElse(null);

        if(guest != null){
            if(!guest.getFullName().equals(dtoFullName)){
                guest.setFullName(dtoFullName);
            }
            if(!guest.getBirthDate().equals(dtoBirthDate)){
                guest.setBirthDate(dtoBirthDate);
                guest.setAge(Math.abs((int) ChronoUnit.YEARS.between(LocalDate.now(), dtoBirthDate)));
            }
            if(!guest.getEmail().equals(dtoEmail)){
                guest.setEmail(dtoEmail);
            }
            if(!guest.getEGN().equals(dtoEgn)){
                guest.setEGN(dtoEgn);
            }
            if(!guest.getPhone().equals(dtoPhone)){
                guest.setPhone(dtoPhone);
            }
            if(!guest.getPassportNumber().equals(dtoPassport)){
                guest.setPassportNumber(dtoPassport);
            }

            this.guestRepository.save(guest);

            return this.mapper.map(guest, GuestViewDto.class);

        }
        return null;
    }

    private Instant dateToInstant(LocalDate date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return date.plusDays(1).atStartOfDay(zoneId).toInstant();
    }
}
