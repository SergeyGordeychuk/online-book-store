package onlinebookstore.service.role;

import onlinebookstore.model.Role;

public interface RoleService {
    Role save(Role role);
    Role findById(Long id);
}
