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

import org.dungeon.game.Engine;
import org.dungeon.game.Entity;
import org.dungeon.game.Game;
import org.dungeon.game.Weight;
import org.dungeon.util.Percentage;

public class Item extends Entity {

  private final int maxIntegrity;
  private final boolean repairable;
  private final boolean weapon;
  private final int damage;
  private final double hitRate;
  private final int integrityDecrementOnHit;
  private final Weight weight;
  private int curIntegrity;
  private FoodComponent foodComponent;
  private ClockComponent clockComponent;
  private BookComponent bookComponent;

  public Item(ItemBlueprint bp) {
    super(bp.id, bp.type, bp.name);

    weight = bp.weight;

    repairable = bp.repairable;
    maxIntegrity = bp.maxIntegrity;
    curIntegrity = bp.curIntegrity;

    weapon = bp.weapon;
    damage = bp.damage;
    hitRate = bp.hitRate;
    integrityDecrementOnHit = bp.integrityDecrementOnHit;

    if (bp.food) {
      foodComponent = new FoodComponent(bp.nutrition, bp.integrityDecrementOnEat);
    }

    if (bp.clock) {
      clockComponent = new ClockComponent();
      clockComponent.setMaster(this);
    }
    if (bp.book) {
      bookComponent = new BookComponent(bp.getSkill());
    }
  }

  public Weight getWeight() {
    if (isFood()) {
      Percentage integrityPercentage = new Percentage(curIntegrity / (double) maxIntegrity);
      return weight.multiply(integrityPercentage);
    } else {
      return weight;
    }
  }

  public String getQualifiedName() {
    if (getCurIntegrity() == getMaxIntegrity()) {
      return getName();
    } else {
      return getIntegrityString() + " " + getName();
    }
  }

  int getMaxIntegrity() {
    return maxIntegrity;
  }

  public int getCurIntegrity() {
    return curIntegrity;
  }

  public void setCurIntegrity(int curIntegrity) {
    if (curIntegrity > 0) {
      this.curIntegrity = curIntegrity;
    } else {
      this.curIntegrity = 0;
      // TODO: maybe we should extract the "breaking routine" to another method.
      if (isClock()) {
        // A clock just broke! Update its last time record.
        clockComponent.setLastTime(Game.getGameState().getWorld().getWorldDate());
      }
    }
  }

  public boolean isRepairable() {
    return repairable;
  }

  public boolean isWeapon() {
    return weapon;
  }

  public int getDamage() {
    return damage;
  }

  double getHitRate() {
    return hitRate;
  }

  int getIntegrityDecrementOnHit() {
    return integrityDecrementOnHit;
  }

  public boolean isFood() {
    return foodComponent != null;
  }

  public FoodComponent getFoodComponent() {
    return foodComponent;
  }

  public boolean isClock() {
    return clockComponent != null;
  }

  public ClockComponent getClockComponent() {
    return clockComponent;
  }

  public BookComponent getBookComponent() {
    return bookComponent;
  }

  public boolean isBroken() {
    return getCurIntegrity() == 0;
  }

  public void incrementIntegrity(int integrityIncrement) {
    setCurIntegrity(Math.min(getCurIntegrity() + integrityIncrement, getMaxIntegrity()));
  }

  public void decrementIntegrityByHit() {
    setCurIntegrity(getCurIntegrity() - getIntegrityDecrementOnHit());
  }

  public void decrementIntegrity(int integrityDecrement) {
    setCurIntegrity(getCurIntegrity() - integrityDecrement);
  }

  public boolean rollForHit() {
    return getHitRate() > Engine.RANDOM.nextDouble();
  }

  // TODO: consider making an enum out of this.
  String getIntegrityString() {
    String weaponIntegrity;
    if (getCurIntegrity() == getMaxIntegrity()) {
      weaponIntegrity = "";
    } else if (getCurIntegrity() >= getMaxIntegrity() * 0.65) {
      weaponIntegrity = "Slightly Damaged";
    } else if (getCurIntegrity() >= getMaxIntegrity() * 0.3) {
      weaponIntegrity = "Damaged";
    } else if (getCurIntegrity() > 0) {
      weaponIntegrity = "Severely Damaged";
    } else {
      weaponIntegrity = "Broken";
    }
    return weaponIntegrity;
  }

  @Override
  public String toString() {
    return getName();
  }

}
