package cgm.model.enums;

import lombok.Getter;

@Getter
public enum CabinType {
    INSIDE("Inside"),
    OUTSIDE("Outside"),
    BALCONY("Balcony"),
    SUITE("Suite");

    public final String label;

    CabinType(String label){
        this.label = label;
    }
}
