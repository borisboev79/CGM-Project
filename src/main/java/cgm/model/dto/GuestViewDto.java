package cgm.model.dto;

import cgm.model.entity.Cabin;
import cgm.model.entity.CruiseGroup;
import cgm.model.entity.Guest;
import cgm.model.entity.Ship;
import cgm.model.enums.AgeGroup;
import cgm.model.enums.CabinType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestViewDto {

    private Long cabinNumber;

    private String cabinCode;

    private String cabinType;

    private String fullName;

    private String email;

    private String phone;

    private Instant birthDate;
    private Integer age;

    private String EGN;

    private String passportNumber;

}
