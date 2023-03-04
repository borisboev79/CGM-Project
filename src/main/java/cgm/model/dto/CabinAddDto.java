package cgm.model.dto;

import cgm.model.entity.CruiseGroup;
import cgm.model.entity.Ship;
import cgm.model.enums.CabinType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CabinAddDto {

    @NotBlank
    @Size(max = 5)
    private String cabinCode;

    @Enumerated(EnumType.STRING)
    private CabinType type;

    @NotNull
    private Ship ship;

    @NotNull
    private CruiseGroup cruiseGroup;

    @Min(2)
    @Max(4)
    private Integer maxOccupancy;

    @NotNull
    @Positive
    private Double adultPrice;

    @NotNull
    @Positive
    private Double childPrice;

    @NotNull
    @Positive
    private Double extraAdultPrice;

}
