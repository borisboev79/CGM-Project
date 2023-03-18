package cgm.model.dto;

import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;
import cgm.util.validation.ValidateUserExistence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationDto {


    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, message = "Username too short")
    @ValidateUserExistence(message = "This username already exists!")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 3, message = "First name too short")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 3, message="Last name too short")
    private String lastName;

    @NotNull(message = "There should be at least one role selected!")
    private List<Role> roles;

    @NotNull(message = "Choosing a branch is mandatory.")
    private BranchCode branch;
}
