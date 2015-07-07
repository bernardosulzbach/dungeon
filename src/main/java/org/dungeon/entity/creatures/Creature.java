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

package org.dungeon.entity.creatures;

import org.dungeon.entity.Entity;
import org.dungeon.entity.LightSource;
import org.dungeon.entity.Luminosity;
import org.dungeon.entity.TagSet;
import org.dungeon.entity.items.CreatureInventory;
import org.dungeon.entity.items.Item;
import org.dungeon.game.Location;
import org.dungeon.io.DLogger;
import org.dungeon.skill.SkillList;
import org.dungeon.skill.SkillRotation;
import org.dungeon.stats.CauseOfDeath;

import java.util.ArrayList;

/**
 * The Creature class.
 */
public class Creature extends Entity {

  private final int maxHealth;
  private final int attack;
  private final AttackAlgorithmID attackAlgorithmID;
  private final SkillList skillList = new SkillList();
  private final SkillRotation skillRotation = new SkillRotation();
  private final TagSet<Tag> tagSet;
  private final CreatureInventory inventory;
  private final LightSource lightSource;
  private int curHealth;
  private Item weapon;
  private Location location;

  public Creature(CreaturePreset preset) {
    super(preset);
    maxHealth = preset.getHealth();
    curHealth = preset.getHealth();
    attack = preset.getAttack();
    tagSet = TagSet.copyTagSet(preset.getTagSet());
    attackAlgorithmID = preset.getAttackAlgorithmID();
    inventory = new CreatureInventory(this, preset.getInventoryItemLimit(), preset.getInventoryWeightLimit());
    lightSource = new LightSource(preset.getLuminosity());
  }

  SkillList getSkillList() {
    return skillList;
  }

  public SkillRotation getSkillRotation() {
    return skillRotation;
  }

  public boolean hasTag(Tag tag) {
    return tagSet.hasTag(tag);
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

  public CreatureInventory getInventory() {
    return inventory;
  }

  @Override
  public Luminosity getLuminosity() {
    return lightSource.getLuminosity();
  }

  public Item getWeapon() {
    return weapon;
  }

  /**
   * Sets an Item as the currently equipped weapon. The Item must be in this Creature's inventory and have the WEAPON
   * tag.
   *
   * @param weapon an Item that must be in this Creature's inventory and have the WEAPON tag
   */
  public void setWeapon(Item weapon) {
    if (inventory.hasItem(weapon)) {
      if (weapon.hasTag(Item.Tag.WEAPON)) {
        this.weapon = weapon;
      } else {
        DLogger.warning(String.format("Tried to equip %s (no WEAPON tag) on %s!", weapon.getName(), getName()));
      }
    } else {
      DLogger.warning("Tried to equip an Item that is not in the inventory of " + getName() + "!");
    }
  }

  /**
   * Unequips the currently equipped weapon.
   */
  public void unsetWeapon() {
    this.weapon = null;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  /**
   * Hits the specified target Creature. Returns what would be the CauseOfDeath if the target died for this attack.
   *
   * @param target the target
   * @return the possible CauseOfDeath
   */
  public CauseOfDeath hit(Creature target) {
    return AttackAlgorithms.renderAttack(this, target);
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
   * Effectively drops an item.
   *
   * @param item the Item to be dropped
   */
  void dropItem(Item item) {
    if (getInventory().hasItem(item)) {
      getInventory().removeItem(item);
      getLocation().addItem(item);
    }
  }

  /**
   * Iterates though the Creature's inventory, dropping all Items on the ground.
   */
  public void dropEverything() {
    for (Item item : new ArrayList<Item>(getInventory().getItems())) {
      getInventory().removeItem(item);
      getLocation().addItem(item);
    }
  }

  public AttackAlgorithmID getAttackAlgorithmID() {
    return attackAlgorithmID;
  }

  @Override
  public String toString() {
    return getName().getSingular();
  }

  public enum Tag {MILKABLE, CORPSE}

}
