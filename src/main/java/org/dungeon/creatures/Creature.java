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

import org.dungeon.game.Entity;
import org.dungeon.game.ID;
import org.dungeon.game.Location;
import org.dungeon.game.Name;
import org.dungeon.items.CreatureInventory;
import org.dungeon.items.Item;
import org.dungeon.skill.SkillList;
import org.dungeon.skill.SkillRotation;

/**
 * The Creature class.
 *
 * @author Bernardo Sulzbach
 */
public class Creature extends Entity {

  private final int maxHealth;
  private final int attack;
  private final String attackAlgorithm;
  private final SkillList skillList = new SkillList();
  private final SkillRotation skillRotation = new SkillRotation();
  private int curHealth;
  private CreatureInventory inventory;
  private Item weapon;
  private Location location;

  public Creature(ID id, String type, Name name, int health, int attack, String attackAlgorithm) {
    super(id, type, name);
    maxHealth = curHealth = health;
    this.attack = attack;
    this.attackAlgorithm = attackAlgorithm;
  }

  /**
   * The copy constructor. Used to generate Creatures that populate the World from model creatures.
   */
  Creature(Creature original) {
    this(original.getID(), original.type, original.name, original.maxHealth, original.attack, original.attackAlgorithm);
  }

  SkillList getSkillList() {
    return skillList;
  }

  public SkillRotation getSkillRotation() {
    return skillRotation;
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

  /**
   * Increments the creature's health by a certain amount, without exceeding its maximum health.
   */
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

  public boolean isAlive() {
    return getCurHealth() > 0;
  }

  public boolean isDead() {
    return !isAlive();
  }

  boolean hasWeapon() {
    return getWeapon() != null;
  }

}
