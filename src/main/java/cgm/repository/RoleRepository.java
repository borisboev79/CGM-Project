package cgm.repository;

import cgm.model.entity.RoleEntity;
import cgm.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findRoleEntityByRole(Role role);
}
