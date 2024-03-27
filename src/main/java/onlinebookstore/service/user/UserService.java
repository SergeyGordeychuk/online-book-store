package onlinebookstore.service.user;

import onlinebookstore.dto.user.UserRegistrationRequestDto;
import onlinebookstore.dto.user.UserResponseDto;
import onlinebookstore.exception.RegistrationException;

public interface UserService {

    UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;

}
