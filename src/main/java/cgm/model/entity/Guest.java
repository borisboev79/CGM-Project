package cgm.model.entity;

import cgm.model.enums.AgeGroup;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="guests")
public class Guest extends BaseEntity{

    @Column(name = "full-name", nullable = false)
    private String fullName;

    private String email;

    private String phone;

    @Column(name = "birth-date")
    private Instant birthDate;

    @Column
    private Integer age;

    @Column
    private String EGN;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Column(name = "passport-number")
    private String passportNumber;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Cabin cabin;

}
