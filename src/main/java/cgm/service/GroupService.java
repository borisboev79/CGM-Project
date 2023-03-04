package cgm.service;

import cgm.model.dto.GroupAddDto;
import cgm.model.entity.Cabin;
import cgm.model.entity.CruiseGroup;
import cgm.model.entity.Ship;
import cgm.repository.GroupRepository;
import cgm.repository.ShipRepository;
import cgm.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ShipRepository shipRepository;
    private final ModelMapper mapper;

    private CruiseGroup currentGroup;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository, ShipRepository shipRepository, ModelMapper mapper) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.shipRepository = shipRepository;

        this.mapper = mapper;
        this.currentGroup =  new CruiseGroup();
    }

    public void createGroup(GroupAddDto groupAddDto, String principalUsername) {

        CruiseGroup group = mapper.map(groupAddDto, CruiseGroup.class);
        group.setEmployee(this.userRepository.findUserEntityByUsername(principalUsername).orElseThrow());
        group.setShip(this.shipRepository.findShipByName(groupAddDto.getShip()).orElseThrow());

        this.groupRepository.saveAndFlush(group);

        this.currentGroup = group;
    }

    public void addCabins(){
        List<Cabin> cabins = new ArrayList<>();
        Ship currentShip = currentGroup.getShip();
        CruiseGroup cruiseGroup = currentGroup;



    }


}
