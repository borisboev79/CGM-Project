package cgm.model.entity;

import cgm.model.enums.CabinType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="cabins")
public class Cabin extends BaseEntity{

    @Column(name="cabin-code", nullable = false)
    private String cabinCode;

    @Enumerated(EnumType.STRING)
    private CabinType type;

    @ManyToOne(optional = false)
    private Ship ship;

    @ManyToOne
    private CruiseGroup cruiseGroup;

    @Column(name = "max-occupancy", nullable = false)
    private Integer maxOccupancy;

    @Column(name = "adt-price", nullable = false)
    private Double adultPrice;

    @Column(name = "chd-price", nullable = false)
    private Double childPrice;

    @Column(name = "extra-adt-price", nullable = false)
    private Double extraAdultPrice;

    @Column
    private Integer count;

    @OneToMany(mappedBy = "cabin", targetEntity = Guest.class, fetch = FetchType.EAGER)
    private List<Guest> guests;


}
