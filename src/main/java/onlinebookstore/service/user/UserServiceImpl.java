package onlinebookstore.service.user;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.user.UserRegistrationRequestDto;
import onlinebookstore.dto.user.UserResponseDto;
import onlinebookstore.exception.RegistrationException;
import onlinebookstore.mapper.UserMapper;
import onlinebookstore.model.User;
import onlinebookstore.repository.user.UserRepository;
import onlinebookstore.service.role.RoleService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setShippingAddress(request.getShippingAddress());
        user.setRoles(Set.of(roleService.findById(2L)));// Default role "ROLE_USER".
        User saveUser = userRepository.save(user);
        return userMapper.toDto(saveUser);
    }
}
