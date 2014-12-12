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

package org.dungeon.creatures;

import org.dungeon.game.Location;
import org.dungeon.items.CreatureInventory;
import org.dungeon.items.Item;

/**
 * The Creature class.
 *
 * @author Bernardo Sulzbach
 */
public class Creature extends Entity {

    private int maxHealth;
    private int curHealth;

    private int attack;
    private String attackAlgorithm;

    private CreatureInventory inventory;
    private Item weapon;

    private Location location;

    public Creature(CreatureBlueprint bp) {
        super(bp.getId(), bp.getType(), bp.getName());
        attackAlgorithm = bp.getAttackAlgorithmID();
        attack = bp.getAttack();
        maxHealth = bp.getMaxHealth();
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

    int getMaxHealth() {
        return maxHealth;
    }

    int getCurHealth() {
        return curHealth;
    }

    void setCurHealth(int curHealth) {
        this.curHealth = curHealth;
    }

    public int getAttack() {
        return attack;
    }

    String getAttackAlgorithm() {
        return attackAlgorithm;
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
