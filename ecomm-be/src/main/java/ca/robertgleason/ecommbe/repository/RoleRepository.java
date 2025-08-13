package ca.robertgleason.ecommbe.repository;

import ca.robertgleason.ecommbe.model.AppRole;
import ca.robertgleason.ecommbe.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {


    Optional<Role> findByRoleName(AppRole appRole);
}
