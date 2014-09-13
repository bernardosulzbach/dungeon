package game;

import java.io.Serializable;

public class Weapon extends Item implements Serializable {

    private static final String TYPE = "Weapon";
    
    /**
     * How much damage the weapon makes.
     */
    private int damage;

    /**
     * How often (from 0 [never] to 100 [always]) does the weapon miss.
     */
    private int missRate;

    /**
     * Weapon integrity variables.
     */
    private int maxIntegrity;
    private int curIntegrity;
    // How much integrity is lost per hit.
    private int hitDecrement;

    public Weapon(String name, int damage) {
        this(name, damage, 0, 100, 100, 1);
    }

    public Weapon(String name, int damage, int missRate) {
        this(name, damage, missRate, 100, 100, 1);
    }

    public Weapon(String name, int damage, int missRate, int maxIntegrity, int curIntegrity, int hitDecrement) {
        super(name);
        this.damage = damage;
        this.missRate = missRate;
        this.maxIntegrity = maxIntegrity;
        this.curIntegrity = curIntegrity;
        this.hitDecrement = hitDecrement;
    }

    public int getDamage() {
        return damage;
    }

    public final void setDamage(int damage) {
        if (damage < 0) {
            this.damage = 0;
        } else {
            this.damage = damage;
        }
    }

    public int getMissRate() {
        return missRate;
    }

    public final void setMissRate(int missRate) {
        if (missRate < 0) {
            this.missRate = 0;
        } else if (missRate > 100) {
            this.missRate = 100;
        } else {
            this.missRate = missRate;
        }
    }

    public int getMaxIntegrity() {
        return maxIntegrity;
    }

    public int getCurIntegrity() {
        return curIntegrity;
    }

    public final boolean isBroken() {
        return curIntegrity == 0;
    }

    /**
     * Randomly evaluates if the next attack of this weapon will miss.
     *
     * @return true if the weapon will miss, false otherwise.
     */
    public final boolean isMiss() {
        /**
         * Implementation info.
         *
         * 0, for instance, will never be greater than 0 (the smallest possible value on the right-hand side).
         *
         * So a missRate of 0 works as intended [isMiss always returns false].
         *
         * 100 will, on the other hand, always be bigger than 99 (the biggest possible value on the right-hand side).
         *
         * So a missRate of 100 also works as intended [isMiss always returns true].
         */
        return missRate > Game.RANDOM.nextInt(100);
    }

    /**
     * Decrements the weapon's integrity.
     */
    public final void decrementIntegrity() {
        if (curIntegrity - hitDecrement > 0) {
            curIntegrity -= hitDecrement;
        } else {
            curIntegrity = 0;
            Game.writeString(getName() + " broke.");
        }
    }

    /**
     * Restores the weapon's integrity to its maximum value.
     */
    public final void repair() {
        curIntegrity = maxIntegrity;
    }

    public final String getStatusString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("  %-20s%10s\n", "Weapon", getName()));
        builder.append(String.format("  %-20s%10s\n", "Weapon damage", getDamage()));
        builder.append(String.format("  %-20s%10s\n", "Weapon integrity",
                String.format("%d/%d", getCurIntegrity(), getMaxIntegrity())));
        return builder.toString();
    }
    
    @Override
    public String toShortString() {
        return String.format("  [%s] %-20s Damage: %d", TYPE, getName(), damage);
    }

}
