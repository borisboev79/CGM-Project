package cgm.repository;

import cgm.model.entity.CruiseLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CruiseLineRepository extends JpaRepository<CruiseLine, Long> {
}
