package onlinebookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import onlinebookstore.validation.FieldMatch;

@Data
@FieldMatch
public class UserRegistrationRequestDto {
    @Email
    @NotNull
    @Size(min = 8, max = 20)
    private String email;
    @NotNull
    @Size(min = 8, max = 100)
    private String password;
    @NotNull
    @Size(min = 8, max = 100)
    private String repeatPassword;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String shippingAddress;
}
