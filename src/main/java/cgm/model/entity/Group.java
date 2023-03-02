package cgm.model.entity;

import cgm.model.enums.Transportation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="cruise-groups")
public class Group extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Transportation transportation;

    @Column(name = "start-date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end-date", nullable = false)
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String itinerary;

    @Column(nullable = false, name ="image-url")
    private String imageUrl;

    @Column(name = "total-pax", nullable = false)
    private Integer totalPax;

    @OneToMany
    private List<Cabin> cabins;

    @ManyToOne
    private UserEntity employee;





}
