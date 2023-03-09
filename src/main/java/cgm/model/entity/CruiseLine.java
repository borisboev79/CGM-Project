package cgm.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="cruise-lines")
public class CruiseLine extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name="logo-url")
    private String logoUrl;

    @OneToMany(mappedBy = "cruiseLine", targetEntity = Ship.class, fetch = FetchType.EAGER)
    private Set<Ship> ships;


}
