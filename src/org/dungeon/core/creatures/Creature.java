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

import org.dungeon.core.game.Location;
import org.dungeon.core.items.Inventory;
import org.dungeon.core.items.Item;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;
import org.dungeon.utils.StringUtils;

import java.io.Serializable;

/**
 * The Creature class.
 *
 * @author Bernardo Sulzbach
 */
public class Creature implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String type;
    private final String name;

    private int level;
    private int experience;
    private int experienceDrop;
    private int maxHealth;
    private int curHealth;
    private int healthIncrement;

    private int attack;
    private int attackIncrement;
    private String attackAlgorithm;

    private Inventory inventory;
    private Item weapon;

    private Location location;

    public Creature(CreatureBlueprint bp) {
        id = bp.getId();
        type = bp.getType();
        name = bp.getName();
        attackAlgorithm = bp.getAttackAlgorithmID();
        // TODO: add support to different levels or remove the levelling system already.
        level = 1;
        attack = bp.getAttack();
        attackIncrement = bp.getAttackIncrement();
        experienceDrop = level * bp.getExperienceDropFactor();
        maxHealth = bp.getMaxHealth() + (level - 1) * bp.getMaxHealthIncrement();
        curHealth = bp.getCurHealth();
    }

    Creature(String id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.attackAlgorithm = "CRITTER";
    }

    //
    //
    // Getters and setters.
    //
    //
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    int getLevel() {
        return level;
    }

    void setLevel(int level) {
        this.level = level;
    }

    int getExperience() {
        return experience;
    }

    public int getExperienceDrop() {
        return experienceDrop;
    }

    int getMaxHealth() {
        return maxHealth;
    }

    void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    int getCurHealth() {
        return curHealth;
    }

    void setCurHealth(int curHealth) {
        this.curHealth = curHealth;
    }

    int getHealthIncrement() {
        return healthIncrement;
    }

    void setHealthIncrement(int healthIncrement) {
        this.healthIncrement = healthIncrement;
    }

    public int getAttack() {
        return attack;
    }

    void setAttack(int attack) {
        this.attack = attack;
    }

    int getAttackIncrement() {
        return attackIncrement;
    }

    void setAttackIncrement(int attackIncrement) {
        this.attackIncrement = attackIncrement;
    }

    String getAttackAlgorithm() {
        return attackAlgorithm;
    }

    void setAttackAlgorithm(String attackAlgorithm) {
        this.attackAlgorithm = attackAlgorithm;
    }

    public Inventory getInventory() {
        return inventory;
    }

    void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Item getWeapon() {
        return weapon;
    }

    public void setWeapon(Item weapon) {
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
    void addHealth(int amount) {
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
    int getExperienceToNextLevel() {
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
    void levelUp() {
        setLevel(getLevel() + 1);
        setMaxHealth(getMaxHealth() + getHealthIncrement());
        setCurHealth(getMaxHealth());
        setAttack(getAttack() + getAttackIncrement());
        if (this.getLevel() % 3 == 0) {
            this.getInventory().setItemLimit(this.getInventory().getItemLimit() + 1);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.centerString(Constants.LEVEL_UP, '-')).append("\n");
        sb.append(String.format("%s is now level %d.", getName(), getLevel())).append("\n");
        sb.append(String.format("Level %d progress: %d / %d", getLevel() + 1, getExperience(), getExperienceToNextLevel())).append("\n");
        IO.writeString(sb.toString());
    }


    //
    //
    // Combat methods.
    //
    //
    public void hit(Creature target) {
        AttackAlgorithm.attack(this, target, getAttackAlgorithm());
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
    // Predicate methods.
    //
    //

    /**
     * Checks if the creature is alive.
     */
    public boolean isAlive() {
        return getCurHealth() > 0;
    }

    /**
     * Checks if the creature is dead.
     */
    public boolean isDead() {
        return getCurHealth() == 0;
    }

    /**
     * Checks if the creature has a weapon.
     */
    public boolean hasWeapon() {
        return getWeapon() != null;
    }

}
