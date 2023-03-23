package cgm.model.dto;

import cgm.model.enums.BranchCode;
import cgm.model.enums.CabinType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CabinViewDto {

    private Long id;
    private String cabinCode;

    private CabinType type;
    private Double adultPrice;

    private Double childPrice;

    private Double extraAdultPrice;

    private List<GuestViewDto> guests;

    private double totalPrice;

    private Integer maxOccupancy;

    private int paxNumber;

    private BranchCode addedByCode;
    private boolean isFull;


}
