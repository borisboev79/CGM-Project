package cgm.model.enums;

public enum Transportation {
    BUS("Bus"),
    AIRPLANE("Flight"),
    CAR("Car");

    public final String label;

    Transportation(String label) {
        this.label = label;
    }
}
