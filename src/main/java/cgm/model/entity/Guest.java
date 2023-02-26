package cgm.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

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
    private LocalDate birthDate;

    @Column
    private Integer age;

    @Column
    private String EGN;

    @Column(name = "passport-number")
    private String passportNumber;

    @ManyToOne
    private Cabin cabin;

}
