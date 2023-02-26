package cgm.model.entity;

import cgm.model.enums.BranchCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="branches")
public class BranchEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private BranchCode code;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "branch", targetEntity = UserEntity.class)
    private Set<UserEntity> employees;
}
