package cgm.service;

import cgm.model.dto.CabinAddDto;
import cgm.model.entity.Cabin;
import cgm.model.entity.CruiseGroup;
import cgm.repository.CabinRepository;
import cgm.repository.GroupRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinService {
    private final CabinRepository cabinRepository;
    private final GroupRepository groupRepository;
    private final ModelMapper mapper;

    @Autowired
    public CabinService(CabinRepository cabinRepository, GroupRepository groupRepository, ModelMapper mapper) {
        this.cabinRepository = cabinRepository;
        this.groupRepository = groupRepository;
        this.mapper = mapper;
    }

    public void addCabin(CabinAddDto cabinAddDto, Long groupId) {


        CruiseGroup group = this.groupRepository.findById(groupId).orElseThrow();

        Cabin cabin = mapper.map(cabinAddDto, Cabin.class);
        cabin.setShip(group.getShip());
        cabin.setCruiseGroup(group);
        group.getCabins().add(cabin);
        this.cabinRepository.saveAndFlush(cabin);


    }


}

