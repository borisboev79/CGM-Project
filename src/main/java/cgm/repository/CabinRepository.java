package cgm.repository;

import cgm.model.entity.Cabin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CabinRepository extends JpaRepository<Cabin, Long> {
}
