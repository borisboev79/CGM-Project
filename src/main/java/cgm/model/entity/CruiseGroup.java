package cgm.model.entity;

import cgm.model.enums.Transportation;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="cruise-groups")
public class CruiseGroup extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Transportation transportation;

    @Column(name = "start-date", nullable = false)
    private Instant startDate;

    @Column(name = "end-date", nullable = false)
    private Instant endDate;

    @Column
    private Long duration;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String itinerary;

    @Column(nullable = false, name ="image-url")
    private String imageUrl;

    @Column(name = "total-pax", nullable = false)
    private Integer totalPax;

    @ManyToOne(optional = false)
    private UserEntity employee;

    @ManyToOne(optional = false)
    private Ship ship;

    @OneToMany
    private List<Cabin> cabins;

    @Override
    public String toString() {
        return "CruiseGroup{" +
                "name='" + name + '\'' +
                ", transportation=" + transportation +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", itinerary='" + itinerary + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", totalPax=" + totalPax +
                ", employee=" + employee +
                ", cabins=" + cabins +
                '}';
    }
}
