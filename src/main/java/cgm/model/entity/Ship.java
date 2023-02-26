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
@Table(name="ships")
public class Ship extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private CruiseLine cruiseLine;

    @OneToMany(mappedBy = "ship", targetEntity = Cabin.class)
    private Set<Cabin> cabins;
}
