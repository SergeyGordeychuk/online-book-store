package onlinebookstore.service.role;

import lombok.RequiredArgsConstructor;
import onlinebookstore.model.Role;
import onlinebookstore.repository.role.RoleRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find role by id " + id));
    }
}
