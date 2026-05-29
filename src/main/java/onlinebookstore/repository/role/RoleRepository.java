package onlinebookstore.repository.role;

import java.util.Optional;
import onlinebookstore.model.Role;
import onlinebookstore.model.Role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
