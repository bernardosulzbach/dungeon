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

import java.io.Serializable;

import org.dungeon.core.game.Location;
import org.dungeon.core.game.Selectable;
import org.dungeon.core.items.Weapon;
import org.dungeon.io.IO;
import org.dungeon.utils.Utils;

/**
 * The Creature class.
 *
 * @author Bernardo Sulzbach
 */
public class Creature implements Serializable, Selectable {

    private static final long serialVersionUID = 1L;

    private CreatureID id;

    private String name;

    private int level;
    private int experience;
    private int experienceDrop;

    private int gold;

    private int maxHealth;
    private int curHealth;
    private int healthIncrement;

    private int attack;
    private int attackIncrement;

    private Weapon weapon;
    private Location location;

    // The empty constructor
    public Creature() {

    }

    //
    //
    // Factory methods.
    //
    //
    public static Creature createCreature(CreaturePreset preset, int level) {
        Creature creature = new Creature();
        creature.setId(preset.getId());
        creature.setName(preset.getId().getName());
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
    public CreatureID getId() {
        return id;
    }

    public void setId(CreatureID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public final void setName(String name) {
        if (Utils.isValidName(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Invalid name.");
        }
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

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
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
        setMaxHealth(getMaxHealth() + healthIncrement);
        setCurHealth(getMaxHealth());
        setAttack(getAttack() + attackIncrement);
        IO.writeString(String.format("%s leveld up. %s is now level %d.", getName(), getName(), getLevel()));
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
    /**
     * Disarm the creature. Placing its current weapon, if any, in the ground.
     */
    public void dropWeapon() {
        if (getWeapon() != null) {
            location.addItem(getWeapon());
            IO.writeString(getName() + " dropped " + getWeapon().getName() + ".");
            setWeapon(null);
        } else {
            IO.writeString("You are not currently carrying a weapon.");
        }
    }

    public void equipWeapon(Weapon weapon) {
        this.setWeapon(weapon);
        IO.writeString(getName() + " equipped " + weapon.getName() + ".");
    }

    //
    //
    // Combat methods.
    //
    //
    /**
     * Try to hit a target. If the creature has a weapon, it will be used to perform the attack. Otherwise, the creature will attack with
     * its bare hands.
     *
     * @param target
     */
    public void hit(Creature target) {
        int hitDamage;
        // Check that there is a weapon and that it is not broken.
        if (getWeapon() != null && !getWeapon().isBroken()) {
            // Check if the attack is a miss.
            if (getWeapon().isMiss()) {
                IO.writeString(getName() + " missed.");
                return;
            } else {
                hitDamage = getWeapon().getDamage();
                target.takeDamage(hitDamage);
                getWeapon().decrementIntegrity();
            }
        } else {
            hitDamage = this.getAttack();
            target.takeDamage(hitDamage);
        }
        IO.writeString(String.format("%s inflicted %d damage points to %s.\n", getName(), hitDamage, target.getName()));
    }

    public void takeDamage(int damage) {
        if (damage > getCurHealth()) {
            setCurHealth(0);
        } else {
            setCurHealth(getCurHealth() - damage);
        }
    }

    //
    //
    // Helper methods.
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

    //
    //
    // Selectable implementation.
    //
    //
    @Override
    public String toSelectionEntry() {
        return String.format("%-20s Level %2d", getName(), getLevel());
    }

}
