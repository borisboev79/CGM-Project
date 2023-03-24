package cgm.model.entity;

import cgm.model.enums.Transportation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
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

    @Column(nullable = false, length = 1000)
    private String itinerary;

    @Column(nullable = false, name ="image-url")
    private String imageUrl;

    @Column(name = "total-pax", nullable = false)
    private Integer totalPax;

    @ManyToOne(optional = false)
    private UserEntity employee;

    @ManyToOne(optional = false)
    private Ship ship;

    @OneToMany(mappedBy = "cruiseGroup", targetEntity = Cabin.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Cabin> cabins;

    @Column
    @Value("false")
    private boolean isSoldOut;

    @Column(columnDefinition = "integer default 0")
    private int soldPax;

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
