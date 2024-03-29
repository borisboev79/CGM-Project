package cgm.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserViewDto {

    private Long id;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String roles;

    private String branch;

    @Override
    public String toString() {
        return "UserViewDto{"+ id + " " +
                username + " " +
                firstName + " " +
                lastName + " [" +
                roles + "] " + branch.toString() + '}';
    }
}
