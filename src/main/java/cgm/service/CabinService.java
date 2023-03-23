package cgm.service;

import cgm.model.ObjectNotFoundException;
import cgm.model.dto.CabinAddDto;
import cgm.model.entity.BranchEntity;
import cgm.model.entity.Cabin;
import cgm.model.entity.CruiseGroup;
import cgm.repository.CabinRepository;
import cgm.repository.GroupRepository;
import cgm.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CabinService {
    private final CabinRepository cabinRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public CabinService(CabinRepository cabinRepository, GroupRepository groupRepository, UserRepository userRepository, ModelMapper mapper) {
        this.cabinRepository = cabinRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
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
        return this.cabinRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "cabin"));
    }

    public void closeCabin(Cabin cabinToClose, String username) {

        BranchEntity addedBy = Objects.requireNonNull(this.userRepository.findUserEntityByUsername(username).orElse(null)).getBranch();

        cabinToClose.setFull(true);
        cabinToClose.setAddedBy(addedBy);

        this.cabinRepository.save(cabinToClose);
    }
}

