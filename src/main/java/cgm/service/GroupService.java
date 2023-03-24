package cgm.service;

import cgm.model.ObjectNotFoundException;
import cgm.model.dto.CabinViewDto;
import cgm.model.dto.GroupAddDto;
import cgm.model.dto.GroupViewDto;
import cgm.model.entity.Cabin;
import cgm.model.entity.CruiseGroup;
import cgm.model.entity.Ship;
import cgm.repository.GroupRepository;
import cgm.repository.ShipRepository;
import cgm.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
        Ship ship = this.shipRepository.findShipByName(groupAddDto.getShip()).orElseThrow();

        group.setStartDate(dateToInstant(groupAddDto.getStartDate()));
        group.setEndDate(dateToInstant(groupAddDto.getEndDate()));
        group.setDuration(groupAddDto.getEndDate().toEpochDay() - groupAddDto.getStartDate().toEpochDay());
        group.setEmployee(this.userRepository.findUserEntityByUsername(principalUsername).orElseThrow());
        group.setShip(ship);

        this.groupRepository.saveAndFlush(group);

        return group;
    }

    private Instant dateToInstant(LocalDate date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return date.plusDays(1).atStartOfDay(zoneId).toInstant();
    }

    public List<GroupViewDto> getAllGroups() {

        List<GroupViewDto> groups = new ArrayList<>();

        for (CruiseGroup group : this.groupRepository.findAll()) {

            List<CabinViewDto> cabins = group.getCabins()
                    .stream()
                    .map(cabin -> mapper.map(cabin, CabinViewDto.class))
                    .toList();

            GroupViewDto groupView = this.mapper.map(group, GroupViewDto.class);
            groupView.setCabins(cabins);

            groups.add(groupView);
        }

        return groups;
    }


    public GroupViewDto findById(Long id) {

        CruiseGroup cruiseGroup = this.groupRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "group"));

        return mapper.map(cruiseGroup, GroupViewDto.class);

    }

    public void checkAvailability(CruiseGroup cruiseGroup) {
        List<Cabin> freeCabins = cruiseGroup.getCabins().stream().filter(cabin -> !cabin.isFull()).toList();
        if (freeCabins.isEmpty()) {
            cruiseGroup.setSoldOut(true);
            this.groupRepository.save(cruiseGroup);
        }
    }

    @Transactional
    public void deleteGroupById(Long id) {
        CruiseGroup groupToDelete = this.groupRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "group"));

        this.groupRepository.delete(groupToDelete);
    }

}
