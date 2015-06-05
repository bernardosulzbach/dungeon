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

import org.dungeon.entity.Preset;
import org.dungeon.entity.TagSet;
import org.dungeon.entity.Visibility;
import org.dungeon.entity.Weight;
import org.dungeon.game.ID;
import org.dungeon.game.Name;
import org.dungeon.io.DLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * CreaturePreset class that stores the information that the CreatureFactory uses to produce creatures.
 */
public final class CreaturePreset implements Preset {

  private final TagSet<Creature.Tag> tagSet = TagSet.makeEmptyTagSet(Creature.Tag.class);
  private ID id;
  private String type;
  private Name name;
  private Weight weight;
  private int health;
  private int attack;
  private ID attackAlgorithmID;
  private List<ID> items = new ArrayList<ID>();
  private Visibility visibility;

  /**
   * Ensures that an integer value is greater than or equal to a provided minimum.
   * If it is not, returns the minimum and logs a warning.
   *
   * @param value         the original value
   * @param minimum       the minimum acceptable value
   * @param attributeName the name of the attribute this value represents (used for logging)
   * @return an integer i such that i >= minimum
   */
  private static int validate(int value, int minimum, String attributeName) {
    if (value >= minimum) {
      return value;
    } else {
      String s = String.format("Attempted to set %d to %s in CreaturePreset. Using %d.", value, attributeName, minimum);
      DLogger.warning(s);
      return minimum;
    }
  }

  TagSet<Creature.Tag> getTagSet() {
    return tagSet;
  }

  public boolean hasTag(Creature.Tag tag) {
    return tagSet.hasTag(tag);
  }

  public void addTag(Creature.Tag tag) {
    tagSet.addTag(tag);
  }

  public ID getID() {
    return id;
  }

  public void setID(ID id) {
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

  public ID getAttackAlgorithmID() {
    return attackAlgorithmID;
  }

  public void setAttackAlgorithmID(ID attackAlgorithmID) {
    this.attackAlgorithmID = attackAlgorithmID;
  }

  public List<ID> getItems() {
    return items;
  }

  public void setItems(List<ID> items) {
    this.items = items;
  }

  public org.dungeon.entity.Visibility getVisibility() {
    return visibility;
  }

  public void setVisibility(Visibility visibility) {
    this.visibility = visibility;
  }

}
