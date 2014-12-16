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

public final class ItemBlueprint {

  // Identification fields.
  ID id;
  String type;
  String name;

  // Durability fields.
  int maxIntegrity;
  int curIntegrity;
  boolean repairable;

  // Weapon fields.
  boolean weapon;
  int damage;
  double hitRate;
  int integrityDecrementOnHit;

  // Food fields.
  boolean food;
  int nutrition;
  int integrityDecrementOnEat;

  // Clock field.
  boolean clock;

  public ID getId() {
    return id;
  }

  public void setId(String id) {
    this.id = new ID(id);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getMaxIntegrity() {
    return maxIntegrity;
  }

  public void setMaxIntegrity(int maxIntegrity) {
    this.maxIntegrity = maxIntegrity;
  }

  public int getCurIntegrity() {
    return curIntegrity;
  }

  public void setCurIntegrity(int curIntegrity) {
    this.curIntegrity = curIntegrity;
  }

  public boolean isRepairable() {
    return repairable;
  }

  public void setRepairable(boolean repairable) {
    this.repairable = repairable;
  }

  public boolean isWeapon() {
    return weapon;
  }

  public void setWeapon(boolean weapon) {
    this.weapon = weapon;
  }

  public int getDamage() {
    return damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

  public double getHitRate() {
    return hitRate;
  }

  public void setHitRate(double hitRate) {
    this.hitRate = hitRate;
  }

  public int getIntegrityDecrementOnHit() {
    return integrityDecrementOnHit;
  }

  public void setIntegrityDecrementOnHit(int integrityDecrementOnHit) {
    this.integrityDecrementOnHit = integrityDecrementOnHit;
  }

  public boolean isFood() {
    return food;
  }

  public void setFood(boolean food) {
    this.food = food;
  }

  public int getNutrition() {
    return nutrition;
  }

  public void setNutrition(int nutrition) {
    this.nutrition = nutrition;
  }

  public int getIntegrityDecrementOnEat() {
    return integrityDecrementOnEat;
  }

  public void setIntegrityDecrementOnEat(int integrityDecrementOnEat) {
    this.integrityDecrementOnEat = integrityDecrementOnEat;
  }

  public boolean isClock() {
    return clock;
  }

  public void setClock(boolean clock) {
    this.clock = clock;
  }

}
