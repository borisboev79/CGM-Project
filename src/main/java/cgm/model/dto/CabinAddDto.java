package cgm.model.dto;

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
    @NotNull
    private CabinType type;

    @Min(2)
    @Max(4)
    @NotNull
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

    @NotNull
    @Positive
    private Integer count;

    @Override
    public String toString() {
        return "CabinAddDto{" +
                "cabinCode='" + cabinCode + '\'' +
                ", type=" + type +
                ", maxOccupancy=" + maxOccupancy +
                ", adultPrice=" + adultPrice +
                ", childPrice=" + childPrice +
                ", extraAdultPrice=" + extraAdultPrice +
                ", count=" + count +
                '}';
    }
}
