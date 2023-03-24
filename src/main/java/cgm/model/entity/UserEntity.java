package cgm.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name="first-name")
    private String firstName;

    @Column(name="last-name", nullable = false)
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<RoleEntity> roles;

    @ManyToOne(optional = false)
    private BranchEntity branch;

    public void clearRoles(){
        roles.clear();
    }


}
