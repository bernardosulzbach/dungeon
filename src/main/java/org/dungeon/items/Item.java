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

import java.util.Set;

public class Item extends Entity {

  public enum Tag {WEAPON, FOOD, CLOCK, BOOK, REPAIRABLE, WEIGHT_PROPORTIONAL_TO_INTEGRITY}

  private final Set<Tag> tags;
  private final int maxIntegrity;
  private int curIntegrity;
  private WeaponComponent weaponComponent;
  private FoodComponent foodComponent;
  private ClockComponent clockComponent;
  private BookComponent bookComponent;

  public Item(ItemBlueprint bp) {
    super(bp.id, bp.type, bp.name, bp.weight);

    tags = bp.tags;

    maxIntegrity = bp.maxIntegrity;
    curIntegrity = bp.curIntegrity;

    if (hasTag(Tag.WEAPON)) {
      weaponComponent = new WeaponComponent(bp.damage, bp.hitRate, bp.integrityDecrementOnHit);
    }
    if (hasTag(Tag.FOOD)) {
      foodComponent = new FoodComponent(bp.nutrition, bp.integrityDecrementOnEat);
    }
    if (hasTag(Tag.CLOCK)) {
      clockComponent = new ClockComponent();
      clockComponent.setMaster(this);
    }
    if (hasTag(Tag.BOOK)) {
      bookComponent = new BookComponent(bp.getSkill());
    }
  }

  @Override
  public Weight getWeight() {
    if (hasTag(Tag.WEIGHT_PROPORTIONAL_TO_INTEGRITY)) {
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
      if (hasTag(Tag.CLOCK)) {
        // A clock just broke! Update its last time record.
        clockComponent.setLastTime(Game.getGameState().getWorld().getWorldDate());
      }
    }
  }

  public boolean hasTag(Tag tag) {
    return tags.contains(tag);
  }

  public WeaponComponent getWeaponComponent() {
    return weaponComponent;
  }

  public FoodComponent getFoodComponent() {
    return foodComponent;
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
    setCurIntegrity(getCurIntegrity() - weaponComponent.getIntegrityDecrementOnHit());
  }

  public void decrementIntegrity(int integrityDecrement) {
    setCurIntegrity(getCurIntegrity() - integrityDecrement);
  }

  public boolean rollForHit() {
    return weaponComponent.getHitRate() > Engine.RANDOM.nextDouble();
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
