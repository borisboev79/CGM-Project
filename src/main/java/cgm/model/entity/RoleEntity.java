package cgm.model.entity;

import cgm.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="roles")
public class RoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @Size(max = 100)
    private String description;

    @Override
    public String toString() {
        return role.name();
    }
}
