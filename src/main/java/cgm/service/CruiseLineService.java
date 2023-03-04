package cgm.service;

import cgm.model.entity.CruiseLine;
import cgm.repository.CruiseLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CruiseLineService {
    private final CruiseLineRepository cruiseLineRepository;

    @Autowired
    public CruiseLineService(CruiseLineRepository cruiseLineRepository) {
        this.cruiseLineRepository = cruiseLineRepository;
    }

    public List<CruiseLine> getCruiseLines(){
        return this.cruiseLineRepository.findAll();

    }
}
