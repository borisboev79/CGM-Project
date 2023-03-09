package cgm.repository;

import cgm.model.entity.BranchEntity;
import cgm.model.enums.BranchCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, Long> {

    Optional<BranchEntity> findByName(String name);
    Optional<BranchEntity> findBranchEntityByCode(BranchCode code);
}
