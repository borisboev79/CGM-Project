package cgm.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @OneToMany(mappedBy = "cruiseLine", targetEntity = Ship.class)
    private Set<Ship> ships;


}
