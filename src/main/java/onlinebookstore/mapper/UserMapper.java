package onlinebookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import onlinebookstore.config.MapperConfig;
import onlinebookstore.dto.user.UserRegistrationRequestDto;
import onlinebookstore.dto.user.UserResponseDto;
import onlinebookstore.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    User toModel(UserRegistrationRequestDto responseDto);
}
