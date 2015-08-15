/*
 * Copyright (C) 2015 Bernardo Sulzbach
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

import org.dungeon.entity.Luminosity;
import org.dungeon.entity.Preset;
import org.dungeon.entity.TagSet;
import org.dungeon.entity.Visibility;
import org.dungeon.entity.Weight;
import org.dungeon.game.Id;
import org.dungeon.game.Name;
import org.dungeon.io.DungeonLogger;

import java.util.List;

/**
 * CreaturePreset class that stores the information that the CreatureFactory uses to produce creatures.
 */
public final class CreaturePreset implements Preset {

  private TagSet<Creature.Tag> tagSet;
  private Id id;
  private String type;
  private Name name;
  private Weight weight;
  private int health;
  private int attack;
  private AttackAlgorithmId attackAlgorithmId;
  private List<Id> items;
  private List<Drop> dropList;
  private Visibility visibility;
  private Luminosity luminosity = Luminosity.ZERO;
  private Id weaponId;
  private int inventoryItemLimit;
  private double inventoryWeightLimit;

  /**
   * Ensures that an integer value is greater than or equal to a provided minimum. If it is not, returns the minimum and
   * logs a warning.
   *
   * @param value the original value
   * @param minimum the minimum acceptable value
   * @param attributeName the name of the attribute this value represents (used for logging)
   * @return an integer i such that i >= minimum
   */
  private static int validate(int value, int minimum, String attributeName) {
    if (value >= minimum) {
      return value;
    } else {
      String format = "Attempted to set %d to %s in CreaturePreset. Using %d.";
      DungeonLogger.warning(String.format(format, value, attributeName, minimum));
      return minimum;
    }
  }

  TagSet<Creature.Tag> getTagSet() {
    return tagSet;
  }

  public void setTagSet(TagSet<Creature.Tag> tagSet) {
    this.tagSet = tagSet;
  }

  public boolean hasTag(Creature.Tag tag) {
    return tagSet.hasTag(tag);
  }

  public Id getId() {
    return id;
  }

  public void setId(Id id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Name getName() {
    return name;
  }

  public void setName(Name name) {
    this.name = name;
  }

  public Weight getWeight() {
    return weight;
  }

  public void setWeight(Weight weight) {
    this.weight = weight;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    health = validate(health, 1, "health");
    this.health = health;
  }

  public int getAttack() {
    return attack;
  }

  public void setAttack(int attack) {
    attack = validate(attack, 0, "attack");
    this.attack = attack;
  }

  public AttackAlgorithmId getAttackAlgorithmId() {
    return attackAlgorithmId;
  }

  public void setAttackAlgorithmId(AttackAlgorithmId attackAlgorithmId) {
    this.attackAlgorithmId = attackAlgorithmId;
  }

  public List<Id> getItems() {
    return items;
  }

  public void setItems(List<Id> items) {
    this.items = items;
  }

  public List<Drop> getDropList() {
    return dropList;
  }

  public void setDropList(List<Drop> dropList) {
    this.dropList = dropList;
  }

  public Visibility getVisibility() {
    return visibility;
  }

  public void setVisibility(Visibility visibility) {
    this.visibility = visibility;
  }

  public Luminosity getLuminosity() {
    return luminosity;
  }

  public void setLuminosity(Luminosity luminosity) {
    this.luminosity = luminosity;
  }

  public Id getWeaponId() {
    return weaponId;
  }

  public void setWeaponId(Id weaponId) {
    this.weaponId = weaponId;
  }

  public int getInventoryItemLimit() {
    return inventoryItemLimit;
  }

  public void setInventoryItemLimit(int inventoryItemLimit) {
    this.inventoryItemLimit = inventoryItemLimit;
  }

  public double getInventoryWeightLimit() {
    return inventoryWeightLimit;
  }

  public void setInventoryWeightLimit(double inventoryWeightLimit) {
    this.inventoryWeightLimit = inventoryWeightLimit;
  }

}
