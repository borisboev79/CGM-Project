package cgm.model.dto;

import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserViewDto {

    private Long id;

    private String username;

    private String password = "********";

    private String firstName;

    private String lastName;


    private String roles;

    private String branch;
}
