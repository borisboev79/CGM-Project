package cgm.model.dto;

import cgm.model.enums.Transportation;
import cgm.util.validation.DateAfterDate;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@DateAfterDate
public class GroupAddDto {

    @Size(min = 10)
    @NotBlank
    private String name;


    @NotBlank
    private String ship;

    @Min(10)
    @NotNull
    private Integer totalPax;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Transportation transportation;

    @NotBlank
    private String imageUrl;

    @NotNull
    @FutureOrPresent
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @Size(min = 10)
    @NotBlank
    private String itinerary;


}
