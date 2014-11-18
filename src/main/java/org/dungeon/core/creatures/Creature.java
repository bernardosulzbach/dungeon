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
import org.dungeon.core.items.CreatureInventory;
import org.dungeon.core.items.Item;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Utils;

/**
 * The Creature class.
 *
 * @author Bernardo Sulzbach
 */
public class Creature extends Entity {

    private int level;
    private int experience;
    private int experienceDrop;
    private int maxHealth;
    private int curHealth;
    private int healthIncrement;

    private int attack;
    private int attackIncrement;
    private String attackAlgorithm;

    private CreatureInventory inventory;
    private Item weapon;

    private Location location;

    public Creature(CreatureBlueprint bp) {
        super(bp.getId(), bp.getType(), bp.getName());
        attackAlgorithm = bp.getAttackAlgorithmID();
        // TODO: add support to different levels or remove the levelling system already.
        level = 1;
        attack = bp.getAttack();
        attackIncrement = bp.getAttackIncrement();
        experienceDrop = level * bp.getExperienceDropFactor();
        maxHealth = bp.getMaxHealth() + (level - 1) * bp.getMaxHealthIncrement();
        curHealth = bp.getCurHealth();
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

    double getLevelProgress() {
        int xpToCurLevel = getExperienceToLevel(getLevel());
        return ((double) (getExperience() - xpToCurLevel)) / (getExperienceToNextLevel() - xpToCurLevel);
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

    public CreatureInventory getInventory() {
        return inventory;
    }

    void setInventory(CreatureInventory inventory) {
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

    // Increments the creature's health by a certain amount, never exceeding its maximum health.
    void addHealth(int amount) {
        int sum = amount + getCurHealth();
        if (sum > getMaxHealth()) {
            setCurHealth(getMaxHealth());
        } else {
            setCurHealth(sum);
        }
    }

    private int getExperienceToLevel(int level) {
        return (level - 1) * (level - 1) * 100;
    }

    int getExperienceToNextLevel() {
        return getExperienceToLevel(getLevel() + 1);
    }

    public void addExperience(int amount) {
        this.experience += amount;
        IO.writeString(getName() + " got " + amount + " experience points.");
        if (this.experience >= getExperienceToNextLevel()) {
            levelUp();
        }
    }

    // Increases the creature level by one and writes a message about it.
    void levelUp() {
        setLevel(getLevel() + 1);
        setMaxHealth(getMaxHealth() + getHealthIncrement());
        setCurHealth(getMaxHealth());
        setAttack(getAttack() + getAttackIncrement());
        if (this.getLevel() % 2 == 0) {
            this.getInventory().setLimit(this.getInventory().getLimit() + 1);
        }
        IO.writeString(Utils.centerString(Constants.LEVEL_UP, '-') + '\n'
                + getName() + " is now level " + getLevel() + '.' + '\n'
                + "Level " + (getLevel() + 1) + " progress: "
                + getExperience() + "/" + getExperienceToNextLevel());
    }

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

    // Checks if the creature is alive.
    public boolean isAlive() {
        return getCurHealth() > 0;
    }

    // Checks if the creature is dead.
    public boolean isDead() {
        return !isAlive();
    }

    // Checks if the creature has a weapon.
    public boolean hasWeapon() {
        return getWeapon() != null;
    }

}
