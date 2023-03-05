package cgm.service;

import cgm.model.dto.CabinAddDto;
import cgm.repository.CabinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinService {
    private final CabinRepository cabinRepository;

    @Autowired
    public CabinService(CabinRepository cabinRepository) {
        this.cabinRepository = cabinRepository;
    }

    public void addCabin(CabinAddDto cabinAddDto){


    }
}
