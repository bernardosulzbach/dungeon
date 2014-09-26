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
import org.dungeon.core.game.Selectable;
import org.dungeon.utils.Constants;

import java.io.Serializable;

public class Item implements Selectable, Serializable {

    private static final long serialVersionUID = 1L;

    // Identification fields.
    private String id;
    private String type;
    private String name;

    // Durability fields.
    private int maxIntegrity;
    private int curIntegrity;
    private boolean repairable;

    // Weapon fields.
    private boolean weapon;
    private int damage;
    private double hitRate;
    private int integrityDecrementOnHit;

    // Food fields.
    private boolean food;
    private int nutrition;
    private int integrityDecrementOnEat;

    private Item() {

    }

    public static Item createItem(ItemPreset preset) {
        Item result = new Item();

        result.id = preset.id;
        result.type = preset.type;
        result.name = preset.name;

        result.repairable = preset.repairable;
        result.maxIntegrity = preset.maxIntegrity;
        result.curIntegrity = preset.curIntegrity;

        result.weapon = preset.weapon;
        result.damage = preset.damage;
        result.hitRate = preset.hitRate;
        result.integrityDecrementOnHit = preset.integrityDecrementOnHit;

        result.food = preset.food;
        result.nutrition = preset.nutrition;
        result.integrityDecrementOnEat = preset.integrityDecrementOnEat;

        return result;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxIntegrity() {
        return maxIntegrity;
    }

    public void setMaxIntegrity(int maxIntegrity) {
        this.maxIntegrity = maxIntegrity;
    }

    public int getCurIntegrity() {
        return curIntegrity;
    }

    public void setCurIntegrity(int curIntegrity) {
        if (curIntegrity < 0) {
            this.curIntegrity = 0;
        } else {
            this.curIntegrity = curIntegrity;
        }
    }

    public boolean isRepairable() {
        return repairable;
    }

    public void setRepairable(boolean repairable) {
        this.repairable = repairable;
    }

    public boolean isWeapon() {
        return weapon;
    }

    public void setWeapon(boolean weapon) {
        this.weapon = weapon;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public double getHitRate() {
        return hitRate;
    }

    public void setHitRate(double hitRate) {
        this.hitRate = hitRate;
    }

    public int getIntegrityDecrementOnHit() {
        return integrityDecrementOnHit;
    }

    public void setIntegrityDecrementOnHit(int integrityDecrementOnHit) {
        this.integrityDecrementOnHit = integrityDecrementOnHit;
    }

    public boolean isFood() {
        return food;
    }

    public void setFood(boolean food) {
        this.food = food;
    }

    public int getNutrition() {
        return nutrition;
    }

    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

    public int getIntegrityDecrementOnEat() {
        return integrityDecrementOnEat;
    }

    public void setIntegrityDecrementOnEat(int integrityDecrementOnEat) {
        this.integrityDecrementOnEat = integrityDecrementOnEat;
    }

    // Durability methods
    public boolean isBroken() {
        return getCurIntegrity() == 0;
    }

    public void decrementIntegrityByHit() {
        setCurIntegrity(getCurIntegrity() - getIntegrityDecrementOnHit());
    }

    public void decrementIntegrityByEat() {
        setCurIntegrity(getCurIntegrity() - getIntegrityDecrementOnEat());
    }

    // Weapon methods
    public boolean rollForHit() {
        return getHitRate() > Game.RANDOM.nextDouble();
    }


    // Food methods


    // Selectable implementation
    @Override
    public String toSelectionEntry() {
        String typeString = String.format("[%s]", getType());
        String extraString;
        if (isFood()) {
            extraString = String.format("Nutrition: %4d", getNutrition());
        } else {
            extraString = String.format("Damage: %7d", getDamage());
        }
        return String.format(Constants.SELECTION_ENTRY_FORMAT, typeString, getName(), extraString);
    }

    // Printing methods
    public String getStatusString() {
        StringBuilder builder = new StringBuilder();
        String nameString = getName();
        if (isBroken()) {
            nameString += " (Broken)";
        }
        builder.append(String.format("%-20s%20s\n", "Name", nameString));
        builder.append(String.format("%-20s%20s\n", "Damage", getDamage()));
        // Uses three lines to build the integrity line to improve code readability.
        String integrityFraction = String.format("%d/%d", getCurIntegrity(), getMaxIntegrity());
        String integrityString = String.format("%-20s%20s\n", "Integrity", integrityFraction);
        builder.append(integrityString);
        return builder.toString();
    }
}
