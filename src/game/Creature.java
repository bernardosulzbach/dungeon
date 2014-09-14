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
package game;

import java.io.Serializable;
import java.util.List;
import utils.Utils;

public class Creature implements Serializable {

    protected final CreatureID id;
    protected String name;

    protected int level;
    protected int experience;
    private int experienceDrop;

    protected int gold;

    protected int maxHealth;
    private int healthIncrement;
    protected int curHealth;

    protected int attack;
    private int attackIncrement;

    protected Weapon weapon;
    private Location location;

    public Creature(CreatureID id, int level) {
        switch (id) {
            case BAT:
                setName("Bat");
                this.level = level;
                this.experienceDrop = level * level * 15;
                this.curHealth = this.maxHealth = 12 + 3 * level;
                this.attack = 5 + 2 * level;
                break;
            case BEAR:
                setName("Bear");
                this.level = level;
                this.experienceDrop = level * level * 40;
                this.curHealth = this.maxHealth = 30 + 10 * level;
                this.attack = 13 + 7 * level;
                break;
            case RABBIT:
                setName("Rabbit");
                this.level = level;
                this.experienceDrop = level * level * 10;
                this.curHealth = this.maxHealth = 10 + 2 * level;
                this.attack = 5 + 2 * level;
                break;
            case RAT:
                setName("Rat");
                this.level = level;
                this.experienceDrop = level * level * 10;
                this.curHealth = this.maxHealth = 15 + 5 * level;
                this.attack = 6 + 4 * level;
                break;
            case SPIDER:
                setName("Spider");
                this.level = level;
                this.experienceDrop = level * level * 10;
                this.curHealth = this.maxHealth = 17 + 8 * level;
                this.attack = 10 + 5 * level;
                break;
            case WOLF:
                setName("Wolf");
                this.level = level;
                this.experienceDrop = level * level * 20;
                this.curHealth = this.maxHealth = 24 + 6 * level;
                this.attack = 10 + 4 * level;
                break;
            case ZOMBIE:
                setName("Zombie");
                this.level = level;
                this.experienceDrop = level * level * 25;
                this.curHealth = this.maxHealth = 30 + 6 * level;
                this.attack = 12 + 4 * level;
                break;
            default:
                break;
        }
        this.id = id;
    }

    public Creature(String name, int level, int health, int attack, CreatureID id) {
        this.name = name;
        this.level = level;
        this.curHealth = health;
        this.maxHealth = health;
        this.attack = attack;
        this.id = id;
    }

    public CreatureID getId() {
        return id;
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

    public int getExperience() {
        return experience;
    }

    public int getExperienceDrop() {
        return experienceDrop;
    }

    public int getExperienceToNextLevel() {
        return level * level * 100;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void addExperience(int amount) {
        this.experience += amount;
        IO.writeString(name + " got " + amount + " experience points.");
        if (this.experience >= getExperienceToNextLevel()) {
            levelUp();
        }
    }

    /**
     * Increases the creature level by one and writes a message about it.
     */
    public void levelUp() {
        level++;
        maxHealth += healthIncrement;
        curHealth = maxHealth;
        attack += attackIncrement;
        IO.writeString(String.format("%s leveld up. %s is now level %d.", name, name, level));
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        if (this.gold < 0) {
            this.gold = 0;
        }
    }

    public void addGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
            IO.writeString(name + " got " + amount + " gold coins.");
        }
    }

    /**
     * Reduces the creature gold by a given amount.
     *
     * Gold will never become negative, so you should check that the creature has enough gold before subtracting any.
     *
     * @param amount
     */
    public void subtractGold(int amount) {
        if (this.gold - amount > 0) {
            this.gold = this.gold - amount;
        } else {
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

    /**
     * Check if the creature is alive.
     *
     * @return
     */
    public boolean isAlive() {
        return curHealth > 0;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * Disarm the creature. Placing its current weapon, if any, in the ground.
     */
    public void dropWeapon() {
        if (weapon != null) {
            location.addItem(weapon);
            IO.writeString(name + " dropped " + weapon.getName() + ".");
            weapon = null;
        } else {
            IO.writeString("You are not currently carrying a weapon.");
        }
    }

    public void equipWeapon(Weapon weapon) {
        this.weapon = weapon;
        IO.writeString(name + " equipped " + weapon.getName() + ".");
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Try to hit a target. If the creature has a weapon, it will be used to perform the attack. Otherwise, the creature
     * will attack with its bare hands.
     *
     * @param target
     */
    public void hit(Creature target) {
        int hitDamage;
        // Check that there is a weapon and that it is not broken.
        if (weapon != null && !weapon.isBroken()) {
            // Check if the attack is a miss.
            if (weapon.isMiss()) {
                IO.writeString(name + " missed.");
            } else {
                hitDamage = weapon.getDamage();
                target.takeDamage(hitDamage);
                weapon.decrementIntegrity();
                System.out.printf("%s inflicted %d damage points to %s.\n", name, hitDamage, target.getName());
            }
        } else {
            hitDamage = this.attack;
            target.takeDamage(hitDamage);
            System.out.printf("%s inflicted %d damage points to %s.\n", name, hitDamage, target.getName());
        }
    }

    private void takeDamage(int damage) {
        if (damage > curHealth) {
            curHealth = 0;
        } else {
            curHealth -= damage;
        }
    }

    public String toShortString() {
        return String.format("%-20s Level %2d", name, level);
    }
}
