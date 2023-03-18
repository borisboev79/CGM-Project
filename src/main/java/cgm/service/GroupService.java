package cgm.service;

import cgm.model.dto.GroupAddDto;
import cgm.model.entity.Cabin;
import cgm.model.entity.CruiseGroup;
import cgm.repository.GroupRepository;
import cgm.repository.ShipRepository;
import cgm.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ShipRepository shipRepository;
    private final ModelMapper mapper;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository, ShipRepository shipRepository, ModelMapper mapper) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.shipRepository = shipRepository;

        this.mapper = mapper;
    }


    public CruiseGroup createGroup(GroupAddDto groupAddDto, String principalUsername) {

        CruiseGroup group = mapper.map(groupAddDto, CruiseGroup.class);

        group.setStartDate(dateToInstant(groupAddDto.getStartDate()));
        group.setEndDate(dateToInstant(groupAddDto.getEndDate()));
        group.setDuration(groupAddDto.getEndDate().toEpochDay() - groupAddDto.getStartDate().toEpochDay());
        group.setEmployee(this.userRepository.findUserEntityByUsername(principalUsername).orElseThrow());
        group.setShip(this.shipRepository.findShipByName(groupAddDto.getShip()).orElseThrow());

        this.groupRepository.saveAndFlush(group);

        return group;
    }

    private Instant dateToInstant(LocalDate date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return date.plusDays(1).atStartOfDay(zoneId).toInstant();
    }

    public List<CruiseGroup> getAllGroups() {
        return this.groupRepository.findAll();
    }

    public CruiseGroup findById(Long id) {
        return this.groupRepository.findById(id).orElse(null);
    }

    public void checkAvailability(CruiseGroup cruiseGroup) {
        List<Cabin> freeCabins = cruiseGroup.getCabins().stream().filter(cabin -> !cabin.isFull()).toList();
        if(freeCabins.isEmpty()){
            cruiseGroup.setSoldOut(true);
            this.groupRepository.save(cruiseGroup);
        }
    }

    @Transactional
    public void deleteGroupById(Long id){
        CruiseGroup groupToDelete = this.groupRepository.findById(id).orElse(null);

        if(groupToDelete != null) {
            this.groupRepository.delete(groupToDelete);
            System.out.println(groupToDelete);
        }
    }
}
