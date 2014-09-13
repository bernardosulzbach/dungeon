package game;

public enum CreatureID {

    BAT("Bat"),
    BEAR("Bear"),
    HERO("Hero"),
    MAGE("Mage"),
    RABBIT("Rabbit"),
    RAT("Rat"),
    SPIDER("Spider"),
    WOLF("Wolf"),
    ZOMBIE("Zombie");

    private final String stringRepresentation;

    CreatureID(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
