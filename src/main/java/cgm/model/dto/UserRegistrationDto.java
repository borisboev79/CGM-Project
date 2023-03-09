package cgm.model.dto;

import cgm.model.entity.BranchEntity;
import cgm.model.entity.RoleEntity;
import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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


    @NotBlank
    @Size(min = 3)
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Size(min = 3)
    private String firstName;

    @NotBlank
    @Size(min = 3)
    private String lastName;

    @NotNull
    private List<Role> roles;

    @NotNull
    private BranchCode branch;
}
