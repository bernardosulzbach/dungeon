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

package org.dungeon.entity.items;

import org.dungeon.date.Date;
import org.dungeon.date.Period;
import org.dungeon.entity.Entity;
import org.dungeon.entity.LightSource;
import org.dungeon.entity.Luminosity;
import org.dungeon.entity.TagSet;
import org.dungeon.entity.Weight;
import org.dungeon.game.Game;
import org.dungeon.game.Random;
import org.dungeon.io.DLogger;
import org.dungeon.util.Percentage;

public final class Item extends Entity {

  private final int maxIntegrity;
  private final Date dateOfCreation;
  private final long decompositionPeriod;
  private final TagSet<Tag> tagSet;
  private final LightSource lightSource;
  private int curIntegrity;
  private WeaponComponent weaponComponent;
  private FoodComponent foodComponent;
  private ClockComponent clockComponent;
  private BookComponent bookComponent;
  /* The Inventory this Item is in. Should be null whenever this Item is not in an Inventory. */
  private BaseInventory inventory;

  public Item(ItemBlueprint bp, Date date) {
    super(bp);

    tagSet = TagSet.copyTagSet(bp.tagSet);
    dateOfCreation = date;

    decompositionPeriod = bp.putrefactionPeriod;

    maxIntegrity = bp.maxIntegrity;
    curIntegrity = bp.curIntegrity;

    lightSource = new LightSource(bp.getLuminosity());

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
      bookComponent = new BookComponent(bp.getSkill(), bp.text);
    }
  }

  @Override
  public Weight getWeight() {
    Weight weight = super.getWeight();
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
    String singularName = getName().getSingular();
    if (getCurIntegrity() == getMaxIntegrity()) {
      return singularName;
    } else {
      return getIntegrityString() + " " + singularName;
    }
  }

  private int getMaxIntegrity() {
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
    if (!hasTag(Tag.REPAIRABLE)) {
      inventory.removeItem(this);
      return; // The Item object will disappear from the game, don't worry about its state.
    }
    if (hasTag(Tag.CLOCK)) {
      // A clock just broke! Update its last time record.
      clockComponent.setLastTime(Game.getGameState().getWorld().getWorldDate());
    }
  }

  public boolean hasTag(Tag tag) {
    return tagSet.hasTag(tag);
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

  public void setInventory(BaseInventory inventory) {
    this.inventory = inventory;
  }

  public boolean isBroken() {
    return getCurIntegrity() == 0;
  }

  public void incrementIntegrity(int integrityIncrement) {
    setCurIntegrity(getCurIntegrity() + integrityIncrement);
  }

  public void decrementIntegrityByHit() {
    decrementIntegrity(weaponComponent.getIntegrityDecrementOnHit());
  }

  public void decrementIntegrityByEat() {
    decrementIntegrity(foodComponent.getIntegrityDecrementOnEat());
  }

  /**
   * The method that should ultimately be called to decrement the integrity of an item.
   *
   * @param decrement how much to decrement, must be positive
   */
  private void decrementIntegrity(int decrement) {
    if (decrement <= 0) {
      DLogger.warning("Got nonpositive integrity decrement value for a " + getName() + "!");
      throw new IllegalArgumentException("Integrity decrement must be positive!");
    }
    if (isBroken()) {
      DLogger.warning("Attempted to decrement the integrity of an already broken " + getName() + "!");
    }
    setCurIntegrity(getCurIntegrity() - decrement);
  }

  /**
   * Rolls for a hit.
   *
   * @return true if the next attack should hit, false otherwise
   */
  public boolean rollForHit() {
    return Random.roll(weaponComponent.getHitRate());
  }

  private String getIntegrityString() {
    return IntegrityState.getIntegrityState(getCurIntegrity(), getMaxIntegrity()).toString();
  }

  public long getDecompositionPeriod() {
    return decompositionPeriod;
  }

  @Override
  public Luminosity getLuminosity() {
    return lightSource.getLuminosity();
  }

  @Override
  public String toString() {
    return getName().toString();
  }

  public enum Tag {WEAPON, FOOD, CLOCK, BOOK, DECOMPOSES, REPAIRABLE, WEIGHT_PROPORTIONAL_TO_INTEGRITY}

}
