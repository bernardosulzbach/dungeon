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

import org.dungeon.date.Date;
import org.dungeon.date.Period;
import org.dungeon.game.Engine;
import org.dungeon.game.Entity;
import org.dungeon.game.Game;
import org.dungeon.game.Weight;
import org.dungeon.util.Percentage;

import java.util.Set;

public class Item extends Entity {

  private final Set<Tag> tags;
  private final int maxIntegrity;
  private final Date dateOfCreation;
  private final long decompositionPeriod;
  private int curIntegrity;
  private WeaponComponent weaponComponent;
  private FoodComponent foodComponent;
  private ClockComponent clockComponent;
  private BookComponent bookComponent;

  public Item(ItemBlueprint bp, Date date) {
    super(bp.id, bp.type, bp.name, bp.weight);

    tags = bp.tags;

    dateOfCreation = date;

    decompositionPeriod = bp.putrefactionPeriod;

    maxIntegrity = bp.maxIntegrity;
    curIntegrity = bp.curIntegrity;

    if (hasTag(Tag.WEAPON)) {
      weaponComponent = new WeaponComponent(bp.damage, bp.hitRate, bp.integrityDecrementOnHit);
    }
    if (hasTag(Tag.FOOD)) {
      foodComponent = new FoodComponent(bp.nutrition, bp.integrityDecrementOnEat);
    }
    if (hasTag(Tag.CLOCK)) {
      clockComponent = new ClockComponent(this);
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

  /**
   * Returns how many seconds have passed since this Item was created.
   *
   * @return a long representing an amount of seconds
   */
  public long getAge() {
    Period existence = new Period(dateOfCreation, Game.getGameState().getWorld().getWorldDate());
    return existence.getSeconds();
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

  /**
   * Sets the current integrity attribute of this Item to the specified value.
   * If the supplied value is bigger than the maximum integrity, this maximum allowed value is used.
   * Similarly, if the provided value is smaller than zero, zero is used.
   *
   * @param curIntegrity the wanted new integrity for the item
   */
  public void setCurIntegrity(int curIntegrity) {
    if (curIntegrity <= 0) {
      setIntegrityToZero();
    } else {
      this.curIntegrity = Math.min(curIntegrity, maxIntegrity);
    }
  }

  /**
   * This method should be used to set the integrity to zero because it also manages item breaking.
   */
  private void setIntegrityToZero() {
    this.curIntegrity = 0;
    if (hasTag(Tag.CLOCK)) {
      // A clock just broke! Update its last time record.
      clockComponent.setLastTime(Game.getGameState().getWorld().getWorldDate());
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
    setCurIntegrity(getCurIntegrity() + integrityIncrement);
  }

  public void decrementIntegrityByHit() {
    setCurIntegrity(getCurIntegrity() - weaponComponent.getIntegrityDecrementOnHit());
  }

  public void decrementIntegrity(int integrityDecrement) {
    setCurIntegrity(getCurIntegrity() - integrityDecrement);
  }

  /**
   * Rolls for a hit.
   *
   * @return true if the next attack should hit, false otherwise
   */
  public boolean rollForHit() {
    return Engine.roll(weaponComponent.getHitRate());
  }

  String getIntegrityString() {
    return IntegrityState.getIntegrityState(getCurIntegrity(), getMaxIntegrity()).toString();
  }

  @Override
  public String toString() {
    return getName();
  }

  public long getDecompositionPeriod() {
    return decompositionPeriod;
  }

  public enum Tag {WEAPON, FOOD, CLOCK, BOOK, DECOMPOSES, REPAIRABLE, WEIGHT_PROPORTIONAL_TO_INTEGRITY}

}
