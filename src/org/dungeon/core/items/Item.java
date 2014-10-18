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

import org.dungeon.core.creatures.Creature;
import org.dungeon.core.game.Game;
import org.dungeon.core.game.Selectable;
import org.dungeon.utils.Constants;

import java.io.Serializable;

public class Item implements Selectable, Serializable {

    private static final long serialVersionUID = 1L;

    // Ownership.
    private Creature owner;

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
    private FoodComponent food;

    // Clocks.
    private ClockComponent clock;

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

        if (preset.foodComponent != null) {
            result.food = new FoodComponent(preset.foodComponent);
        }

        if (preset.clockComponent != null) {
            result.clock = new ClockComponent(preset.clockComponent);
            result.clock.setMaster(result);
        }

        return result;
    }

    // Getters and setters
    public Creature getOwner() {
        return owner;
    }

    public void setOwner(Creature owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
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

    public int getCurIntegrity() {
        return curIntegrity;
    }

    public void setCurIntegrity(int curIntegrity) {
        if (curIntegrity > 0) {
            this.curIntegrity = curIntegrity;
        } else {
            this.curIntegrity = 0;
            // TODO: maybe we should extract the "breaking routine" to another method.
            if (isClock()) {
                // A clock just broke! Update its last time record.
                clock.setLastTime(getOwner().getLocation().getWorld().getWorldDate());
            }
        }
    }

    public boolean isRepairable() {
        return repairable;
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

    public double getHitRate() {
        return hitRate;
    }

    public int getIntegrityDecrementOnHit() {
        return integrityDecrementOnHit;
    }

    // Food methods
    public boolean isFood() {
        return food != null;
    }

    public FoodComponent getFood() {
        return food;
    }

    // Clock methods
    public boolean isClock() {
        return clock != null;
    }

    public ClockComponent getClock() {
        return clock;
    }

    // Durability methods
    public boolean isBroken() {
        return getCurIntegrity() == 0;
    }

    public void decrementIntegrityByHit() {
        setCurIntegrity(getCurIntegrity() - getIntegrityDecrementOnHit());
    }

    public void decrementIntegrity(int integrityDecrement) {
        setCurIntegrity(getCurIntegrity() - integrityDecrement);
    }

    // Weapon methods
    public boolean rollForHit() {
        return getHitRate() > Game.RANDOM.nextDouble();
    }

    public String getWeaponIntegrity() {
        String weaponIntegrity;
        if (getCurIntegrity() == getMaxIntegrity()) {
            weaponIntegrity = "Not damaged";
        } else if (getCurIntegrity() >= getMaxIntegrity() * 0.65) {
            weaponIntegrity = "Slightly damaged";
        } else if (getCurIntegrity() >= getMaxIntegrity() * 0.3) {
            weaponIntegrity = "Damaged";
        } else if (getCurIntegrity() > 0) {
            weaponIntegrity = "Severely damaged";
        } else {
            weaponIntegrity = "Broken";
        }
        weaponIntegrity = String.format("(%s)", weaponIntegrity);
        return weaponIntegrity;
    }

    // Food methods


    // Selectable implementation
    @Override
    public String toSelectionEntry() {
        String typeString = String.format("[%s]", getType());
        String extraString;
        if (isFood()) {
            extraString = String.format("Nutrition: %4d", getFood().getNutrition());
        } else {
            extraString = String.format("Damage: %7d", getDamage());
        }
        return String.format(Constants.SELECTION_ENTRY_FORMAT, typeString, getName(), extraString);
    }

    // Printing methods
    public String getStatusString() {
        StringBuilder builder = new StringBuilder();
        String nameString = getName();
        builder.append(String.format("%-20s%20s\n", "Name", nameString));
        builder.append(String.format("%-20s%20s\n", "Damage", getDamage()));
        // Uses three lines to build the integrity line to improve code readability.
        String weaponIntegrity = getWeaponIntegrity();
        builder.append(String.format("%-20s%20s\n", "Integrity", weaponIntegrity));
        return builder.toString();
    }


}
