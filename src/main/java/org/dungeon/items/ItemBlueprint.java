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

package org.dungeon.items;

import org.dungeon.game.ID;
import org.dungeon.game.Name;
import org.dungeon.game.Weight;

import java.util.HashSet;

public final class ItemBlueprint {

  ID id;
  String type;
  Name name;

  Weight weight;

  HashSet<Item.Tag> tags;

  int maxIntegrity;
  int curIntegrity;

  int damage;
  double hitRate;
  int integrityDecrementOnHit;

  int nutrition;
  int integrityDecrementOnEat;

  private ID skill;


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

  public HashSet<Item.Tag> getTags() {
    return tags;
  }

  public void setTags(HashSet<Item.Tag> tags) {
    this.tags = tags;
  }

  public void setMaxIntegrity(int maxIntegrity) {
    this.maxIntegrity = maxIntegrity;
  }

  public void setCurIntegrity(int curIntegrity) {
    this.curIntegrity = curIntegrity;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

  public void setHitRate(double hitRate) {
    this.hitRate = hitRate;
  }

  public void setIntegrityDecrementOnHit(int integrityDecrementOnHit) {
    this.integrityDecrementOnHit = integrityDecrementOnHit;
  }

  public void setNutrition(int nutrition) {
    this.nutrition = nutrition;
  }

  public void setIntegrityDecrementOnEat(int integrityDecrementOnEat) {
    this.integrityDecrementOnEat = integrityDecrementOnEat;
  }

  public ID getSkill() {
    return skill;
  }

  public void setSkill(String skill) {
    this.skill = new ID(skill);
  }

}
