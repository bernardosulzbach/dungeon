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
import org.dungeon.game.Location;
import org.dungeon.io.IO;
import org.dungeon.items.CreatureInventory;
import org.dungeon.items.CreatureInventory.AdditionResult;
import org.dungeon.items.Item;
import org.dungeon.skill.SkillList;
import org.dungeon.skill.SkillRotation;
import org.dungeon.stats.CauseOfDeath;

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
  private CreatureInventory inventory = new CreatureInventory(this, 4, 8);
  private Item weapon;
  private Location location;

  public Creature(CreaturePreset preset) {
    super(preset.getID(), preset.getType(), preset.getName());
    maxHealth = preset.getHealth();
    curHealth = preset.getHealth();
    attack = preset.getAttack();
    attackAlgorithm = preset.getAttackAlgorithm();
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

  /**
   * Hits the specified target Creature. Returns what would be the CauseOfDeath if the target died for this attack.
   *
   * @param target the target
   * @return the possible CauseOfDeath
   */
  public CauseOfDeath hit(Creature target) {
    return AttackAlgorithm.attack(this, target, getAttackAlgorithm());
  }

  /**
   * Makes the Creature take a given amount of damage and returns whether its HealthState changed.
   *
   * @param damage the amount of damage the Creature should take
   * @return true if the Creature's HealthState changed
   */
  public boolean takeDamage(int damage) {
    HealthState initialHealthState = HealthState.getHealthState(getCurHealth(), getMaxHealth());
    if (damage > getCurHealth()) {
      setCurHealth(0);
    } else {
      setCurHealth(getCurHealth() - damage);
    }
    HealthState finalHealthState = HealthState.getHealthState(getCurHealth(), getMaxHealth());
    return finalHealthState != initialHealthState;
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

  /**
   * Attempts to add an Item object to this Creature's inventory.
   * <p/>
   * Writes the results of the attempt, for a quiet version of this method, see {@code CreatureInventory.addItem(Item)}.
   *
   * @param item the Item to be added
   * @return true if successful, false otherwise
   */
  public boolean addItem(Item item) {
    AdditionResult result = getInventory().addItem(item);
    switch (result) {
      case AMOUNT_LIMIT:
        IO.writeString("Your inventory is full.");
        break;
      case WEIGHT_LIMIT:
        IO.writeString("You can't carry more weight.");
        break;
      case SUCCESSFUL:
        IO.writeString("Added " + item.getName() + " to the inventory.");
        return true;
    }
    return false;
  }

  /**
   * Effectively drops an item.
   *
   * @param item the Item to be dropped
   */
  public void dropItem(Item item) {
    getInventory().removeItem(item);
    getLocation().addItem(item);
  }

  public void dropEverything() {
    for (Item item : getInventory().getItems()) {
      dropItem(item);
    }
  }

}
