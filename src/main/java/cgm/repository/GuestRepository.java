package cgm.repository;

import cgm.model.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    Optional<List<Guest>> findAllByCabin_CruiseGroup_Id(Long id);
    Optional<List<Guest>> findAllByCabin_Id(Long id);
}
