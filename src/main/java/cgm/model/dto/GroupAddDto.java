package cgm.model.dto;

import cgm.model.enums.Transportation;
import cgm.util.validation.DateAfterDate;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@DateAfterDate(message = "Return date must be after Departure date.")
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

    @NotNull(message = "Start date is mandatory.")
    @FutureOrPresent(message = "Start date cannot be in the past.")
    private LocalDate startDate;

    @NotNull(message = "End date is mandatory.")
    @FutureOrPresent(message = "End date cannot be in the past.")
    private LocalDate endDate;

    @Size(min = 10, max = 1000)
    @NotBlank
    private String itinerary;

    @Override
    public String toString() {
        return "GroupAddDto{" +
                "name='" + name + '\'' +
                ", ship='" + ship + '\'' +
                ", totalPax=" + totalPax +
                ", transportation=" + transportation +
                ", imageUrl='" + imageUrl + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", itinerary='" + itinerary + '\'' +
                '}';
    }
}
