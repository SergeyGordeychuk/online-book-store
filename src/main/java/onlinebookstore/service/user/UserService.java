package onlinebookstore.service.user;

import java.util.List;
import onlinebookstore.dto.user.UserRegistrationRequestDto;
import onlinebookstore.dto.user.UserResponseDto;
import onlinebookstore.exception.RegistrationException;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;

    List<UserResponseDto> findAll(Pageable pageable);

}
