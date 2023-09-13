package onlinebookstore.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.user.UserResponseDto;
import onlinebookstore.service.user.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    List<UserResponseDto> findAll(Authentication authentication, Pageable pageable) {
        return userService.findAll(pageable);
    }
}
