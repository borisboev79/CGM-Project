package cgm.model.dto;

import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;
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


    @NotBlank(message = "Last name is mandatory")
    @Size(min = 3)
    private String username;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 8)
    private String password;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 3)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 3, message="Last name too short")
    private String lastName;

    @NotNull(message = "Last name is mandatory")
    private List<Role> roles;

    @NotNull(message = "Last name is mandatory")
    private BranchCode branch;
}
