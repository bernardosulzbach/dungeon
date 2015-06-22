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
import org.dungeon.game.Game;
import org.dungeon.game.Random;

import java.io.Serializable;

/**
 * The clock component.
 */
public class ClockComponent implements Serializable {

  private final Item master;

  /**
   * Used to store the date the clock had when it was broken.
   */
  private Date lastTime;

  ClockComponent(Item master) {
    this.master = master;
  }

  public void setLastTime(Date lastTime) {
    // Create a new Date object so that this field is not affected by changes in the rest of the program.
    this.lastTime = lastTime;
  }

  /**
   * Returns a string that represents a clock reading.
   */
  public String getTimeString() {
    if (master.isBroken()) {
      if (lastTime == null) {
        if (Random.nextBoolean()) {
          return "The clock is pure junk.";
        } else {
          return "The clock is completely smashed.";
        }
      } else {
        return "The clock is broken. Still, it displays " + lastTime.toTimeString() + ".";
      }
    } else {
      String timeString = Game.getGameState().getWorld().getWorldDate().toTimeString();
      return "The clock displays " + timeString + ".";
    }
  }

}
