package cgm.model.dto;

import cgm.model.enums.Transportation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GroupViewDto {

    private Long id;
    private String name;

    private Long duration;

    private Transportation transportation;

    private Instant startDate;

    private Instant endDate;

    private String itinerary;

    private String imageUrl;

    private Integer totalPax;

    private UserViewDto employee;

    private String shipName;

    private List<CabinViewDto> cabins;

    private boolean isSoldOut;

    private int soldPax;

}
