package cgm.service;

import cgm.model.dto.CabinAddDto;
import cgm.model.entity.Cabin;
import cgm.model.entity.CruiseGroup;
import cgm.model.entity.Guest;
import cgm.repository.CabinRepository;
import cgm.repository.GroupRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        List<Cabin> cabinsToSave = new ArrayList<>();

        for (int i = 0; i < cabinAddDto.getCount(); i++) {
            Cabin cabin = mapper.map(cabinAddDto, Cabin.class);
            cabin.setShip(group.getShip());
            cabin.setCruiseGroup(group);
            group.getCabins().add(cabin);
            cabinsToSave.add(cabin);
        }

        this.cabinRepository.saveAllAndFlush(cabinsToSave);

        if(group.isSoldOut()){
            group.setSoldOut(false);
            this.groupRepository.save(group);
        }


    }


    public Cabin findById(Long id) {
        return this.cabinRepository.findById(id).orElse(null);
    }

    public void closeCabin(Cabin cabinToClose) {

        cabinToClose.setFull(true);

        this.cabinRepository.save(cabinToClose);
    }
}

