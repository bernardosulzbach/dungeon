/* 
 * Copyright (C) 2014 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dungeon.core.items;

import org.dungeon.core.game.Game;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;

public class Weapon extends Item implements IWeapon, Breakable {

    private static final long serialVersionUID = 1L;

    private static final String TYPE = "Weapon";

    private int damage;

    /**
     * How often (from 0 [never] to 100 [always]) does the weapon miss.
     */
    private int missRate;

    // Weapon integrity variables.
    private int maxIntegrity;
    private int curIntegrity;

    /**
     * How much integrity is lost per hit.
     */
    private int hitDecrement;

    protected static Weapon createWeapon(WeaponPreset preset) {
        return new Weapon(preset.name, preset.damage, preset.missRate, preset.startingIntegrity, preset.startingIntegrity, preset.hitDecrement);
    }

    public Weapon(String name, int damage, int missRate) {
        this(name, damage, missRate, 100, 100, 1);
    }

    public Weapon(String name, int damage, int missRate, int maxIntegrity, int curIntegrity, int hitDecrement) {
        super(name, TYPE);
        this.damage = damage;
        this.missRate = missRate;
        this.maxIntegrity = maxIntegrity;
        this.curIntegrity = curIntegrity;
        this.hitDecrement = hitDecrement;
    }

    public int getDamage() {
        return damage;
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
         * Implementation info. 0, for instance, will never be greater than 0 (the smallest possible value on the right-hand side). So a
         * missRate of 0 works as intended [isMiss always returns false]. 100 will, on the other hand, always be bigger than 99 (the biggest
         * possible value on the right-hand side). So a missRate of 100 also works as intended [isMiss always returns true].
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
            IO.writeString(getName() + " broke.");
        }
    }

    /**
     * Restores the weapon's integrity to its maximum value.
     */
    public final void repair() {
        curIntegrity = maxIntegrity;
    }

    // TODO: this code is repeated in Food.java, consider refactoring
    // Maybe food should extend weapon. I cannot think of a food that should not be usable as a weapon.
    @Override
    public String getStatusString() {
        StringBuilder builder = new StringBuilder();
        String nameString = getName();
        if (isBroken()) {
            nameString += " (Broken)";
        }
        builder.append(Constants.MARGIN).append(String.format("%-20s%20s\n", "Name", nameString));
        builder.append(Constants.MARGIN).append(String.format("%-20s%20s\n", "Damage", getDamage()));
        // Uses three lines to build the integrity line to improve code readability.
        String integrityFraction = String.format("%d/%d", getCurIntegrity(), getMaxIntegrity());
        String integrityString = String.format("%-20s%20s\n", "Integrity", integrityFraction);
        builder.append(Constants.MARGIN).append(integrityString);
        return builder.toString();
    }

    @Override
    public String toSelectionEntry() {
        // Use the Item.toSelectionEntry method to avoid code repetition.
        return super.toSelectionEntry() + "Damage: " + getDamage();
    }

}
