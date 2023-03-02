package cgm.repository;

import cgm.model.entity.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ShipRepository extends JpaRepository<Ship,Long> {
    Optional<Set<Ship>> findAllByCruiseLine_Name(String name);
}
