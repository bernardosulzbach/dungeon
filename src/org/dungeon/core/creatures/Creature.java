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
package org.dungeon.core.creatures;

import org.dungeon.core.creatures.enums.CreatureID;
import org.dungeon.core.creatures.enums.CreaturePreset;
import org.dungeon.core.creatures.enums.CreatureType;
import org.dungeon.core.game.Location;
import org.dungeon.core.game.Selectable;
import org.dungeon.core.items.IWeapon;
import org.dungeon.core.items.Inventory;
import org.dungeon.io.IO;

import java.io.Serializable;

/**
 * The Creature class.
 *
 * @author Bernardo Sulzbach
 */
public abstract class Creature implements Serializable, Selectable {

    private static final long serialVersionUID = 1L;

    private final CreatureType type;
    private final CreatureID id;
    private final String name;

    private int level;
    private int experience;
    private int experienceDrop;

    private int gold;

    private int maxHealth;
    private int curHealth;
    private int healthIncrement;

    private int attack;
    private int attackIncrement;

    private Inventory inventory;
    private IWeapon weapon;

    private Location location;

    public Creature(CreatureType type, CreatureID id, String name) {
        this.type = type;
        this.id = id;
        this.name = name;
    }

    //
    //
    // Factory methods.
    //
    //
    public static Creature createCreature(CreaturePreset preset, int level) {
        Creature creature;
        switch (preset.getType()) {
            case CRITTER:
                creature = new Critter(preset.getId(), preset.getId().getName());
                break;
            case BEAST:
                creature = new Beast(preset.getId(), preset.getId().getName());
                break;
            case UNDEAD:
                creature = new Undead(preset.getId(), preset.getId().getName());
                break;
            default:
                return null;
        }
        creature.setLevel(level);
        creature.setExperienceDrop(level * preset.getExperienceDropFactor());
        creature.setMaxHealth(preset.getHealth() + (level - 1) * preset.getHealthIncrement());
        creature.setCurHealth(preset.getHealth() + (level - 1) * preset.getHealthIncrement());
        creature.setAttack(preset.getAttack() + (level - 1) * preset.getAttackIncrement());
        return creature;
    }

    public static Creature[] createCreatureArray(CreaturePreset preset, int level, int amount) {
        Creature[] array = new Creature[amount];
        for (int i = 0; i < amount; i++) {
            array[i] = createCreature(preset, level);
        }
        return array;
    }

    //
    //
    // Getters and setters.
    //
    //
    public CreatureType getType() {
        return type;
    }

    public CreatureID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperienceDrop() {
        return experienceDrop;
    }

    public void setExperienceDrop(int experienceDrop) {
        this.experienceDrop = experienceDrop;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        if (this.gold < 0) {
            this.gold = 0;
        }
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getCurHealth() {
        return curHealth;
    }

    public void setCurHealth(int curHealth) {
        this.curHealth = curHealth;
    }

    public int getHealthIncrement() {
        return healthIncrement;
    }

    public void setHealthIncrement(int healthIncrement) {
        this.healthIncrement = healthIncrement;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAttackIncrement() {
        return attackIncrement;
    }

    public void setAttackIncrement(int attackIncrement) {
        this.attackIncrement = attackIncrement;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public IWeapon getWeapon() {
        return weapon;
    }

    public void setWeapon(IWeapon weapon) {
        this.weapon = weapon;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    //
    //
    // General methods.
    //
    //

    /**
     * Increments the creature's health by a certain amount, never exceeding its maximum health.
     */
    public void addHealth(int amount) {
        int sum = amount + getCurHealth();
        if (sum > getMaxHealth()) {
            setCurHealth(getMaxHealth());
        } else {
            setCurHealth(sum);
        }
    }

    //
    //
    // Leveling methods.
    //
    //
    public int getExperienceToNextLevel() {
        return getLevel() * getLevel() * 100;
    }

    public void addExperience(int amount) {
        this.experience += amount;
        IO.writeString(getName() + " got " + amount + " experience points.");
        if (this.experience >= getExperienceToNextLevel()) {
            levelUp();
        }
    }

    /**
     * Increases the creature level by one and writes a message about it.
     */
    public void levelUp() {
        setLevel(getLevel() + 1);
        setMaxHealth(getMaxHealth() + getHealthIncrement());
        setCurHealth(getMaxHealth());
        setAttack(getAttack() + getAttackIncrement());
        IO.writeString(String.format("%s leveled up. %s is now level %d.", getName(), getName(), getLevel()));
    }

    //
    //
    // Finance methods.
    //
    //
    public void addGold(int amount) {
        if (amount > 0) {
            this.setGold(this.getGold() + amount);
            IO.writeString(getName() + " got " + amount + " gold coins.");
        }
    }

    /**
     * Reduces the creature gold by a given amount. Gold will never become negative, so you should check that the creature has enough gold
     * before subtracting any.
     */
    public void subtractGold(int amount) {
        if (this.getGold() - amount > 0) {
            this.setGold(this.getGold() - amount);
        } else {
            this.setGold(0);
        }
    }

    //
    //
    // Weapon methods.
    //
    //
    public void equipWeapon(IWeapon weapon) {
        this.setWeapon(weapon);
        IO.writeString(getName() + " equipped " + weapon.getName() + ".");
    }

    public void unequipWeapon() {
        if (hasWeapon()) {
            IO.writeString("Unequipped " + getWeapon().getName() + ".");
            this.weapon = null;
        }
    }

    //
    //
    // Combat methods.
    //
    //
    public abstract void hit(Creature target);

    public void takeDamage(int damage) {
        if (damage > getCurHealth()) {
            setCurHealth(0);
        } else {
            setCurHealth(getCurHealth() - damage);
        }
    }

    //
    //
    // Predicate methods.
    //
    //

    /**
     * Checks if the creature is alive.
     *
     * @return
     */
    public boolean isAlive() {
        return getCurHealth() > 0;
    }

    /**
     * Checks if the creature is dead.
     *
     * @return
     */
    public boolean isDead() {
        return getCurHealth() == 0;
    }

    /**
     * Checks if the creature has a weapon.
     *
     * @return
     */
    public boolean hasWeapon() {
        return getWeapon() != null;
    }

    /**
     * Checks if the creature has an inventory.
     */
    public boolean hasInventory() {
        return getInventory() != null;
    }

    //
    //
    // Selectable implementation.
    //
    //
    @Override
    public String toSelectionEntry() {
        return String.format("%-12s%-24s Level %2d", String.format("[%s]", getType()), getName(), getLevel());
    }


}