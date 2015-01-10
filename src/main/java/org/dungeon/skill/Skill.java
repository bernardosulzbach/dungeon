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

package org.dungeon.skill;

import org.dungeon.game.Entity;
import org.dungeon.game.ID;
import org.dungeon.game.Selectable;

/**
 * The Skill class.
 * <p/>
 * Created by Bernardo Sulzbach on 07/01/15.
 */
public class Skill extends Entity implements Selectable {

  private final int damage;
  private final int coolDown;
  private int remainingCoolDown;

  public Skill(String id, String type, String name, int damage, int coolDown) {
    super(new ID(id), type, name);
    this.damage = damage;
    this.coolDown = coolDown;
  }

  /**
   * Returns the damage of this Skill.
   *
   * @return the damage of this Skill.
   */
  public int getDamage() {
    return damage;
  }

  /**
   * Starts the cool down of this Skill.
   */
  public void startCoolDown() {
    remainingCoolDown = coolDown;
  }

  /**
   * Verifies if this Skill is ready to be casted or not.
   */
  public boolean isReady() {
    return remainingCoolDown == 0;
  }

  /**
   * Refreshes the remaining cool down of this Skill.
   * <p/>
   * This method should be invoked after each turn.
   */
  void refresh() {
    if (remainingCoolDown > 0) {
      remainingCoolDown--;
    }
  }

  /**
   * Resets the remaining cool down of this Skill.
   * <p/>
   * This method should be invoked after the battle ends.
   */
  void reset() {
    remainingCoolDown = 0;
  }

}
